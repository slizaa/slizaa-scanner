package org.slizaa.scanner.itest.framework.server;

import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Config.EncryptionLevel;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Type;
import org.neo4j.driver.v1.types.TypeSystem;
import org.neo4j.driver.v1.util.Pair;

public class BoltClient {

  // Driver objects are thread-safe and are typically made available application-wide.
  private Driver driver;

  /**
   * <p>
   * Creates a new instance of type {@link BoltClient}.
   * </p>
   *
   * @param uri
   * @param user
   * @param password
   */
  public BoltClient(String uri) {

    Config config = Config.build().withEncryptionLevel(EncryptionLevel.NONE).toConfig();

    driver = GraphDatabase.driver(uri, config);
  }

  private void printModules() {

    try (Session session = driver.session()) {

      TypeSystem typeSystem = session.typeSystem();

      // Auto-commit transactions are a quick and easy way to wrap a read.
      StatementResult result = session.run("MATCH (m:TYPE) RETURN m");

      int count = 0;

      // Each Cypher execution returns a stream of records.
      while (result.hasNext()) {

        count++;

        Record record = result.next();
        // Values can be extracted from a record by index or name.

        for (Pair<String, Value> pair : record.fields()) {

          System.out.println(pair.key());
          System.out.println(pair.value().type().name());

          switch (pair.value().type().name()) {
          case "NODE": {
            System.out.println("Handle node: " + pair.value().asNode());
            System.out.println(pair.value().asNode().getClass());
            break;
          }
          case "STRING": {
            System.out.println("Handle node: " + pair.value().asString());
            System.out.println(pair.value().asString().getClass());
            break;
          }
          default:
            break;
          }

          System.out.println(pair.value().type());
        }

        // System.out.println("-------------------------------");
        // Node node = record.get("m").asNode();
        // System.out.println("ID: " + node.id());
        // for (String label : node.labels()) {
        // System.out.println(label);
        // }
        // for (String key : node.keys()) {
        // System.out.println(key + " : " + node.get(key));
        // }
      }

      System.out.println("Count: " + count);
    }
  }

  public void close() {
    // Closing a driver immediately shuts down all open connections.
    driver.close();
  }

  public static void main(String... args) {
    BoltClient example = new BoltClient("bolt://localhost:7687");
    example.printModules();
    example.close();
  }
}