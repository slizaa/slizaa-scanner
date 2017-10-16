package org.slizaa.scanner.neo4j.graphdbfactory;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.List;

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

  /**
   * {@inheritDoc}
   */
  @Override
  public IGraphDb createGraphDb(int port, File storeDir) {
    return createGraphDb(port, storeDir, null, null);
  }

  @Override
  public IGraphDb createGraphDb(int port, File storeDir, List<Class<?>> dbExtensions) {
    return createGraphDb(port, storeDir, null, dbExtensions);
  }

  @Override
  public <T> IGraphDb createGraphDb(int port, File storeDir, T userObject) {
    return createGraphDb(port, storeDir, userObject, null);
  }

  @Override
  public <T> IGraphDb createGraphDb(int port, File storeDir, T userObject, List<Class<?>> dbExtensions) {
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
    if (dbExtensions != null) {

      //
      Procedures proceduresService = ((GraphDatabaseAPI) graphDatabase).getDependencyResolver()
          .resolveDependency(Procedures.class);

      for (Class<?> clazz : dbExtensions) {
        try {
          proceduresService.registerFunction(clazz);
          proceduresService.registerProcedure(clazz);
        } catch (KernelException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }

    //
    return new Neo4jGraphDb(graphDatabase, port, userObject);
  }
}
