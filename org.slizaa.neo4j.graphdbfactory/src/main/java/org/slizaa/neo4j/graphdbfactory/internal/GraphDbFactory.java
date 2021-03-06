/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.neo4j.graphdbfactory.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.logging.FormattedLogProvider;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.neo4j.apoc.CreateDerived;
import org.slizaa.neo4j.apoc.SlizaaImportExportProcedures;
import org.slizaa.neo4j.apoc.arch.SlizaaArchProcedures;

import apoc.create.Create;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GraphDbFactory implements IGraphDbFactory {

  private Supplier<List<Class<?>>> _databaseExtensionsSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link GraphDbFactory}.
   * </p>
   *
   */
  public GraphDbFactory() {
    this(null);
  }

  /**
   * <p>
   * Creates a new instance of type {@link GraphDbFactory}.
   * </p>
   *
   * @param databaseExtensionsSupplier
   */
  public GraphDbFactory(Supplier<List<Class<?>>> databaseExtensionsSupplier) {
    this._databaseExtensionsSupplier = databaseExtensionsSupplier;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IGraphDbBuilder newGraphDb(int port, File storeDir) {
    return new GraphDbBuilder(port, storeDir, this._databaseExtensionsSupplier);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IGraphDbBuilder newGraphDb(File databaseDir) {
    return new GraphDbBuilder(-1, databaseDir, this._databaseExtensionsSupplier);
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class GraphDbBuilder implements IGraphDbBuilder {

    /** - */
    private int                      _port;

    /** - */
    private File                     _storeDir;

    /** - */
    private Object                   _userObject;

    /** - */
    private Map<String, Object>      _configuration = new HashMap<>();

    /** - */
    private Supplier<List<Class<?>>> _databaseExtensionsSupplier;

    /**
     * <p>
     * Creates a new instance of type {@link GraphDbBuilder}.
     * </p>
     *
     * @param port
     * @param storeDir
     */
    public GraphDbBuilder(int port, File storeDir, Supplier<List<Class<?>>> databaseExtensionsSupplier) {
      this._port = port;
      this._storeDir = checkNotNull(storeDir);
      this._databaseExtensionsSupplier = databaseExtensionsSupplier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> IGraphDbBuilder withUserObject(T userObject) {

      //
      this._userObject = userObject;

      //
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGraphDbBuilder withConfiguration(String key, Object value) {

      //
      this._configuration.put(key, value);

      //
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGraphDb create() {

      //
      GraphDatabaseFactory factory = new GraphDatabaseFactory();
      factory.setUserLogProvider(FormattedLogProvider.toOutputStream(System.out));

      //
      GraphDatabaseService graphDatabase = null;

      //
      if (this._port != -1) {

        //
        BoltConnector bolt = new BoltConnector("0");
        graphDatabase = factory.newEmbeddedDatabaseBuilder(this._storeDir)
            .setConfig(bolt.type, ConnectorType.BOLT.name()).setConfig(bolt.enabled, "true")
            .setConfig(bolt.listen_address, "localhost:" + this._port).setConfig(bolt.encryption_level, "DISABLED")
            .newGraphDatabase();
      }
      //
      else {

        //
        graphDatabase = factory.newEmbeddedDatabaseBuilder(this._storeDir).newGraphDatabase();
      }

      //
      registerDatabaseExtensions(graphDatabase);

      // the Neo4jGraphDb
      return new Neo4jGraphDb(graphDatabase, this._port, this._userObject);
    }

    /**
     * <p>
     * </p>
     *
     * @param graphDatabase
     */
    private void registerDatabaseExtensions(GraphDatabaseService graphDatabase) {

      //
      List<Class<?>> extensionsToRegister = new LinkedList<>();

      // step 1: _databaseExtensionsSupplier
      if (this._databaseExtensionsSupplier != null) {

        // extract the classes...
        List<Class<?>> classes = this._databaseExtensionsSupplier.get();

        // ...and add them extension list
        if (classes != null) {
          extensionsToRegister.addAll(classes);
        }
      }

      // step 2: neo4j & slizaa-core apoc classess
      extensionsToRegister.addAll(apocListClasses());

      // step 3: neo4j & slizaa-core apoc classess
      extensionsToRegister.addAll(coreApocClasses());

      //
      for (Class<?> clazz : extensionsToRegister) {
        System.out.println("Register extension class: " + clazz.getName());
      }

      // get the procedure service
      Procedures proceduresService = ((GraphDatabaseAPI) graphDatabase).getDependencyResolver()
          .resolveDependency(Procedures.class);

      // register all elements
      for (Class<?> element : extensionsToRegister) {
        try {
          proceduresService.registerFunction(element);
          proceduresService.registerProcedure(element);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private Collection<? extends Class<?>> apocListClasses() {

    //
    List<Class<?>> result = new ArrayList<Class<?>>();

    //
    ClassLoader classLoader = this.getClass().getClassLoader();

    //
    try {

      //
      Enumeration<URL> apocLists = classLoader.getResources("apoc.list");

      if (apocLists != null) {

        //
        while (apocLists.hasMoreElements()) {

          URL url = apocLists.nextElement();

          //
          try (InputStream stream = url.openStream()) {

            //
            List<Class<?>> classesList = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                .lines().map(className -> {
                  try {
                    return classLoader.loadClass(className);
                  } catch (Exception e) {
                    return null;
                  }
                }).filter(v -> v != null).collect(Collectors.toList());

            //
            result.addAll(classesList);
          }
        }
      }
    }

    //
    catch (IOException e) {
      // TODO
      e.printStackTrace();
    }

    return result;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private List<Class<?>> coreApocClasses() {

    //
    List<Class<?>> result = new ArrayList<Class<?>>();

    // add neo4j apoc
    result.add(Create.class);

    // add slizaa apoc
    result.add(SlizaaImportExportProcedures.class);
    result.add(CreateDerived.class);
    result.add(SlizaaArchProcedures.class);

    return result;
  }
}
