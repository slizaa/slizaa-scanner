package org.slizaa.scanner.cmdline;

import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;
import org.neo4j.kernel.configuration.HttpConnector;
import org.neo4j.kernel.configuration.HttpConnector.Encryption;
import org.neo4j.logging.FormattedLogProvider;

import com.beust.jcommander.JCommander;

public class Slizaa {

  /**
   * <p>
   * </p>
   *
   * @param argv
   * @throws IOException
   */
  public static void main(String... argv) throws IOException {

    //
    SlizaaScannerArgs args = new SlizaaScannerArgs();

    //
    JCommander.newBuilder().addObject(args).build().parse(new String[] { "SERVER", "-d",
        "D:\\50-Development\\environments\\slizaa-master\\eclipse\\workspace\\HONK\\.slizaa", "-p", "5001" });

    //
    BoltConnector bolt = new BoltConnector("0");

    //
    GraphDatabaseFactory factory = new GraphDatabaseFactory();
    factory.setUserLogProvider(FormattedLogProvider.toOutputStream(System.out));

    //
    GraphDatabaseService graphDb = factory.newEmbeddedDatabaseBuilder(args.getDirectory())
        .setConfig(bolt.type, ConnectorType.BOLT.name()).setConfig(bolt.enabled, "true")
        .setConfig(bolt.listen_address, "localhost:" + args.getPort()).setConfig(bolt.encryption_level, "DISABLED")
        .newGraphDatabase();

    registerShutdownHook(graphDb);

    System.out.println("Press ENTER to quit.");
    System.in.read();
    graphDb.shutdown();

    System.exit(0);
  }

  // shutdown hook
  private static void registerShutdownHook(final GraphDatabaseService graphDb) {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        graphDb.shutdown();
      }
    });
  }
}
