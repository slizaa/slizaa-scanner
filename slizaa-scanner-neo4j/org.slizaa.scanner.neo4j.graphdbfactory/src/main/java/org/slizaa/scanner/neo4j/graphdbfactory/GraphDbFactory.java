/*******************************************************************************
 * Copyright (C) 2017 wuetherich
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.neo4j.graphdbfactory;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class GraphDbFactory implements IGraphDbFactory {

  /** - */
  public static final String SLIZAA_NEO4J_EXTENSIONCLASSES = "slizaa.neo4j.extensionclasses";

  /**
   * {@inheritDoc}
   */
  @Override
  public IGraphDbBuilder newGraphDb(int port, File storeDir) {
    return new GraphDbBuilder(port, storeDir);
  }

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private class GraphDbBuilder implements IGraphDbBuilder {

    /** - */
    private int                 _port;

    /** - */
    private File                _storeDir;

    /** - */
    private Object              _userObject;

    /** - */
    private Map<String, Object> _configuration = new HashMap<>();

    /**
     * <p>
     * Creates a new instance of type {@link GraphDbBuilder}.
     * </p>
     *
     * @param port
     * @param storeDir
     */
    public GraphDbBuilder(int port, File storeDir) {
      _port = port;
      _storeDir = checkNotNull(storeDir);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> IGraphDbBuilder withUserObject(T userObject) {

      //
      _userObject = userObject;

      //
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGraphDbBuilder withConfiguration(String key, Object value) {

      //
      _configuration.put(key, value);

      //
      return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGraphDb create() {

      GraphDatabaseFactory factory = new GraphDatabaseFactory();
      factory.setUserLogProvider(FormattedLogProvider.toOutputStream(System.out));

      BoltConnector bolt = new BoltConnector("0");

      //
      GraphDatabaseService graphDatabase = factory.newEmbeddedDatabaseBuilder(_storeDir)
          .setConfig(bolt.type, ConnectorType.BOLT.name()).setConfig(bolt.enabled, "true")
          .setConfig(bolt.listen_address, "localhost:" + _port).setConfig(bolt.encryption_level, "DISABLED")
          .newGraphDatabase();

      //
      Object dbExtensions = _configuration.get(SLIZAA_NEO4J_EXTENSIONCLASSES);

      //
      if (dbExtensions != null && dbExtensions instanceof List && !((List<?>) dbExtensions).isEmpty()) {

        //
        Procedures proceduresService = ((GraphDatabaseAPI) graphDatabase).getDependencyResolver()
            .resolveDependency(Procedures.class);

        //
        for (Object element : ((List<?>) dbExtensions)) {

          if (element instanceof Class<?>) {
            try {
              proceduresService.registerFunction((Class<?>) element);
              proceduresService.registerProcedure((Class<?>) element);
            } catch (KernelException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        }
      }

      // the Neo4jGraphDb
      return new Neo4jGraphDb(graphDatabase, _port, _userObject);
    }
  }

}
