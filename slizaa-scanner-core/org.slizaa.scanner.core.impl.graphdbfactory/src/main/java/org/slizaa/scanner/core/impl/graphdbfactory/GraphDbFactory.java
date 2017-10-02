package org.slizaa.scanner.core.impl.graphdbfactory;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

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
  private Supplier<List<Class<?>>> _neo4jExtensionsSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link GraphDbFactory}.
   * </p>
   *
   * @param neo4jExtensionsSupplier
   */
  public GraphDbFactory(Supplier<List<Class<?>>> neo4jExtensionsSupplier) {
    _neo4jExtensionsSupplier = checkNotNull(neo4jExtensionsSupplier);
  }

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
    Procedures proceduresService = ((GraphDatabaseAPI) graphDatabase).getDependencyResolver()
        .resolveDependency(Procedures.class);

    //
    for (Class<?> clazz : _neo4jExtensionsSupplier.get()) {
      try {
        proceduresService.registerFunction(clazz);
        proceduresService.registerProcedure(clazz);
      } catch (KernelException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    //
    return new Neo4jGraphDb(graphDatabase, 5001, userObject);
  }
}
