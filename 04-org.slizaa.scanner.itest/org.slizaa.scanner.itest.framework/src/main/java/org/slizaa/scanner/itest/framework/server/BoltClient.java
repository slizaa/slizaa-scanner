package org.slizaa.scanner.itest.framework.server;

import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Config.EncryptionLevel;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;
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
      // StatementResult result = session.run("MATCH (t:TYPE) WITH t, t.fqn as tfqn MATCH (tref:TYPE_REFERENCE) WHERE
      // tref.fqn = tfqn CREATE (tref)-[:BOUND_TO]->(t) ");
      StatementResult result = session.run("MATCH (t:TYPE) return t.fqn");

      // IMPORTANT!!!
      // StatementResult result = session.run("create index on :TYPE(fqn)");
      // StatementResult result = session.run("create index on :TYPE_REFERENCE(fqn)");
      // StatementResult result = session.run("CALL apoc.export.csv.all('c:/temp/krasserExport.csv',{})");
      // StatementResult result = session.run("CALL slizaa.dump('c:/temp/krasserExport.csv')");

      // StatementResult result = session.run("LOAD CSV WITH HEADERS FROM 'file:///c:/temp/krasserExport.csv' AS row
      // RETURN count(row);");

      // https://stackoverflow.com/questions/28246416/neo4j-export-import-data

      // StatementResult result = session.run("CALL apoc.export.cypher.all('c:/temp/krasserExport.cypher',{})");

      // https://stackoverflow.com/questions/37299077/neo4j-importing-local-csv-file

      int count = 0;

      // Each Cypher execution returns a stream of records.
      while (result.hasNext()) {

        count++;

        Record record = result.next();
        // Values can be extracted from a record by index or name.

        for (Pair<String, Value> pair : record.fields()) {

          // System.out.println(pair.value().type().name());

          switch (pair.value().type().name()) {
          case "NODE": {
            System.out.println("-------------------------------");
            System.out.println(pair.key());
            Node node = pair.value().asNode();
            System.out.println("ID: " + node.id());
            for (String label : node.labels()) {
              System.out.print(label + ", ");
            }
            System.out.println();
            for (String key : node.keys()) {
              System.out.println(key + " : " + node.get(key));
            }
            break;
          }
          case "STRING": {
            System.out.println("STRING: '" + pair.value().asString() + "'");
            break;
          }
          case "INTEGER": {
            System.out.println("INTEGER: " + pair.value().asLong());
            break;
          }
          default:
            System.out.println("UNKNWON: " + pair.value().type());
            break;
          }
        }
      }
    }
  }

  public void close() {
    // Closing a driver immediately shuts down all open connections.
    driver.close();
  }

  public static void main(String... args) {
    BoltClient example = new BoltClient("bolt://localhost:7687");
    // BoltClient example = new BoltClient("bolt://localhost:5001");
    example.printModules();
    example.close();
  }
}