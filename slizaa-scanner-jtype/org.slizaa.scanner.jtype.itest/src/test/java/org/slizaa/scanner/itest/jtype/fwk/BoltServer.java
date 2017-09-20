package org.slizaa.scanner.itest.jtype.fwk;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.api.exceptions.KernelException;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.slizaa.scanner.jtype.graphdbextensions.JTypeProcedures;

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
   * @param storeDir
   * @throws KernelException
   */
  public static final GraphDatabaseService startServer(File storeDir) throws KernelException {

    BoltConnector bolt = new BoltConnector("0");

    GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(storeDir)
        .setConfig(bolt.type, ConnectorType.BOLT.name()).setConfig(bolt.enabled, "true")
        .setConfig(bolt.listen_address, "localhost:7687").setConfig(bolt.encryption_level, "DISABLED")
        .newGraphDatabase();

    // https://github.com/neo4j-contrib/neo4j-apoc-procedures
    // https://neo4j.com/docs/java-reference/3.2/javadocs/index.html?org/neo4j/procedure/Procedure.html
    // https://stackoverflow.com/questions/43965481/how-to-configure-neo4j-embedded-to-run-apoc-procedures
    // https://stackoverflow.com/questions/43184777/export-data-to-csv-files-from-neo4j-pragmatically

    // @Description("Location of the database plugin directory. Compiled Java JAR
    // files that contain database " +
    // "procedures will be loaded if they are placed in this directory.")
    // public static final Setting<File> plugin_dir = pathSetting(
    // "dbms.directories.plugins", "plugins" );
    // TODO: GraphDatabaseSettings.plugin_dir

    // Procedures proceduresService = ((GraphDatabaseAPI)
    // db).getDependencyResolver().resolveDependency(Procedures.class);
    // for (Class<?> procedure : procedures) {
    // proceduresService.registerProcedure(procedure);
    // proceduresService.registerFunction(procedure);
    // }

    Procedures proceduresService = ((GraphDatabaseAPI) graphDb).getDependencyResolver()
        .resolveDependency(Procedures.class);

    proceduresService.registerFunction(JTypeProcedures.class);
    proceduresService.registerProcedure(JTypeProcedures.class);

    return graphDb;
  }

  /**
   * <p>
   * </p>
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws Exception {

    // Wherever the Neo4J storage location is.
    startServer(new File("C:\\Users\\WUETHE~1\\AppData\\Local\\Temp\\slizaaTestDatabases4814019141351147573"));

    System.out.println("Press ENTER to quit.");
    System.in.read();

    System.exit(0);
  }
}
