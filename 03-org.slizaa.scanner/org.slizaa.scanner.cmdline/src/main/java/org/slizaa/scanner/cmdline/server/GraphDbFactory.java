package org.slizaa.scanner.cmdline.server;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.logging.FormattedLogProvider;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;

public class GraphDbFactory implements IGraphDbFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public IGraphDb createGraphDb(int port, File storeDir) {
    return createGraphDb(port, storeDir, null);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ClassNotFoundException
   */
  @Override
  public <T> IGraphDb createGraphDb(int port, File storeDir, T userObject) {
    checkNotNull(storeDir);

    GraphDatabaseFactory factory = new GraphDatabaseFactory();
    factory.setUserLogProvider(FormattedLogProvider.toOutputStream(System.out));

    BoltConnector bolt = new BoltConnector("0");

    //
    GraphDatabaseService graphDatabase = factory.newEmbeddedDatabaseBuilder(storeDir)
        .setConfig(bolt.type, ConnectorType.BOLT.name()).setConfig(bolt.enabled, "true")
        .setConfig(bolt.listen_address, "localhost:" + port).setConfig(bolt.encryption_level, "DISABLED")
        .newGraphDatabase();

    //
    PluginRegistry pluginRegistry = new PluginRegistry();
    pluginRegistry.initialize();

    Procedures proceduresService = ((GraphDatabaseAPI) graphDatabase).getDependencyResolver()
        .resolveDependency(Procedures.class);

    // proceduresService.registerFunction(TestFunction.class);
    // proceduresService.registerProcedure(TestFunction.class);
    // proceduresService.registerFunction(ExportCSV.class);
    // proceduresService.registerProcedure(ExportCSV.class);
    // proceduresService.registerFunction(ExportCypher.class);
    // proceduresService.registerProcedure(ExportCypher.class);

    //
    return new Neo4jGraphDb(graphDatabase, 5001, userObject);
  }
}
