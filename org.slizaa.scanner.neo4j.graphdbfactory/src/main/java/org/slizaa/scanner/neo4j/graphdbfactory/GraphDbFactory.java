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
package org.slizaa.scanner.neo4j.graphdbfactory;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.logging.FormattedLogProvider;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;

import apoc.create.Create;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GraphDbFactory implements IGraphDbFactory {

  /** - */
  public static final String       SLIZAA_NEO4J_EXTENSIONCLASSES = "slizaa.neo4j.extensionclasses";

  /** - */
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

      // step 1: _configuration.get(SLIZAA_NEO4J_EXTENSIONCLASSES)
      Object dbExtensions = this._configuration.get(SLIZAA_NEO4J_EXTENSIONCLASSES);
      if (dbExtensions != null && dbExtensions instanceof List && !((List<?>) dbExtensions).isEmpty()) {

        // extract the classes...
        List<Class<?>> classes = ((List<?>) dbExtensions).stream().filter(e -> e instanceof Class<?>)
            .map(e -> (Class<?>) e).collect(Collectors.toList());

        // ...and add them extension list
        extensionsToRegister.addAll(classes);
      }

      // step 2: _databaseExtensionsSupplier
      if (this._databaseExtensionsSupplier != null) {

        // extract the classes...
        List<Class<?>> classes = this._databaseExtensionsSupplier.get();

        // ...and add them extension list
        if (classes != null) {
          extensionsToRegister.addAll(classes);
        }
      }

      // step 3: neo4j APOC list
      extensionsToRegister.addAll(apocClasses());

      // get the procedure service
      Procedures proceduresService = ((GraphDatabaseAPI) graphDatabase).getDependencyResolver()
          .resolveDependency(Procedures.class);

      // register all elements
      for (Class<?> element : extensionsToRegister) {
        try {
          proceduresService.registerFunction(element);
          proceduresService.registerProcedure(element);
        } catch (KernelException e) {
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
  private List<Class<?>> apocClasses() {

    //
    List<Class<?>> result = new ArrayList<Class<?>>();
    result.add(Create.class);
    return result;

    // //
    // IClasspathScannerFactory factory = ClasspathScannerFactoryBuilder.newClasspathScannerFactory()
    // .registerCodeSourceClassLoaderProvider(Bundle.class, (b) -> {
    // return b.adapt(BundleWiring.class).getClassLoader();
    // }).create();
    //
    // //
    // ClassLoader classLoader = Create.class.getProtectionDomain().getClassLoader();
    //
    // // scan the bundle
    // factory
    //
    // //
    // .createScanner(this._bundle)
    //
    // //
    // .matchClassesWithMethodAnnotation(annotationType, (b, exts) -> {
    // this._extensionsWithMethodAnnotation.put(annotationType, exts);
    // })
    //
    // //
    // .scan();
    //
    // //
    // return this._extensionsWithMethodAnnotation.get(annotationType);
  }
}
