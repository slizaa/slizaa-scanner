package org.slizaa.scanner.itest.framework.server;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BoltServer {

  /**
   * <p>
   * </p>
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    // Wherever the Neo4J storage location is.
    File storeDir = new File(args[0]);

    BoltConnector bolt = new BoltConnector("0");
    // TODO: GraphDatabaseSettings.plugin_dir 
    // ((GraphDatabaseAPI)graphDb).getDependencyResolver().resolveDependency(Procedures.class).register(proc);

    GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(storeDir)
        .setConfig(bolt.type, ConnectorType.BOLT.name()).setConfig(bolt.enabled, "true")
        .setConfig(bolt.listen_address, "localhost:7687").setConfig(bolt.encryption_level, "DISABLED")
        .newGraphDatabase();

    // @Description("Location of the database plugin directory. Compiled Java JAR files that contain database " +
    // "procedures will be loaded if they are placed in this directory.")
    // public static final Setting<File> plugin_dir = pathSetting( "dbms.directories.plugins", "plugins" );
    
    System.out.println("Press ENTER to quit.");
    System.in.read();

    System.exit(0);
  }
}
