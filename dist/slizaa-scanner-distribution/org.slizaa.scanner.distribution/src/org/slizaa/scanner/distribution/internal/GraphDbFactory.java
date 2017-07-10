package org.slizaa.scanner.distribution.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;
import org.neo4j.logging.FormattedLogProvider;
import org.slizaa.product.project.ISlizaaProject;
import org.slizaa.product.project.spi.IGraphDb;
import org.slizaa.product.project.spi.IGraphDbFactory;

public class GraphDbFactory implements IGraphDbFactory {

  GraphDatabaseService temp;

  @Override
  public IGraphDb createGraphDb(ISlizaaProject slizaaProject, File storeDir) {
    checkNotNull(slizaaProject);
    checkNotNull(storeDir);

    GraphDatabaseFactory factory = new GraphDatabaseFactory();
    factory.setUserLogProvider(FormattedLogProvider.toOutputStream(System.out));

    BoltConnector bolt = new BoltConnector("0");

    //
    temp = factory.newEmbeddedDatabaseBuilder(storeDir).setConfig(bolt.type, ConnectorType.BOLT.name())
        .setConfig(bolt.enabled, "true").setConfig(bolt.listen_address, "localhost:5001")
        .setConfig(bolt.encryption_level, "DISABLED").newGraphDatabase();

    System.out.println("BUMM: " + temp);

    //
    Transaction transaction = temp.beginTx();
    System.out.println(temp.getAllLabels().toString());
    transaction.terminate();
    return new Neo4jGraphDb(slizaaProject, temp, 5001);
  }
}
