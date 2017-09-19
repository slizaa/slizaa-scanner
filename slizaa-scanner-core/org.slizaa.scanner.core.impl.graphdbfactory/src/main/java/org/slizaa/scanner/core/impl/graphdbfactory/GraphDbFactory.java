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
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.impl.plugins.SlizaaPluginRegistry;

public class GraphDbFactory implements IGraphDbFactory {

  /** - */
  private Supplier<List<ClassLoader>> _extensionClassLoaderSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link GraphDbFactory}.
   * </p>
   *
   * @param extensionClassLoaderSupplier
   */
  public GraphDbFactory(Supplier<List<ClassLoader>> extensionClassLoaderSupplier) {
    _extensionClassLoaderSupplier = checkNotNull(extensionClassLoaderSupplier);
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

    // get the extension class loaders
    List<ClassLoader> extensionLoaders = _extensionClassLoaderSupplier.get();

    //
    if (extensionLoaders != null) {

      //
      SlizaaPluginRegistry pluginRegistry = new SlizaaPluginRegistry(extensionLoaders);

      // initialize
      pluginRegistry.initialize();

      //
      Procedures proceduresService = ((GraphDatabaseAPI) graphDatabase).getDependencyResolver()
          .resolveDependency(Procedures.class);

      //
      for (Class<?> functionClass : pluginRegistry.getGraphDbUserFunctions()) {
        try {
          System.out.println("***************************************");
          System.out.println(" - " + functionClass.getName());
          System.out.println("***************************************");
          proceduresService.registerFunction(functionClass);
        } catch (KernelException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

      //
      for (Class<?> procedureClass : pluginRegistry.getGraphDbProcedures()) {
        try {
          System.out.println("***************************************");
          System.out.println(" - " + procedureClass.getName());
          System.out.println("***************************************");
          proceduresService.registerProcedure(procedureClass);
        } catch (KernelException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

      //
      proceduresService.getAllProcedures().forEach(prodSig -> System.out.println(prodSig.name()));
    }

    //
    return new Neo4jGraphDb(graphDatabase, 5001, userObject);
  }
}
