package org.slizaa.scanner.distribution.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;
import org.neo4j.logging.FormattedLogProvider;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;

public class GraphDbFactory implements IGraphDbFactory {

  @Override
  public IGraphDb createGraphDb(Object userObject, int port, File storeDir) {
    checkNotNull(userObject);
    checkNotNull(storeDir);

    GraphDatabaseFactory factory = new GraphDatabaseFactory();
    factory.setUserLogProvider(FormattedLogProvider.toOutputStream(System.out));

    BoltConnector bolt = new BoltConnector("0");

    //
    GraphDatabaseService temp = factory.newEmbeddedDatabaseBuilder(storeDir)
        .setConfig(bolt.type, ConnectorType.BOLT.name()).setConfig(bolt.enabled, "true")
        .setConfig(bolt.listen_address, "localhost:" + port).setConfig(bolt.encryption_level, "DISABLED")
        .newGraphDatabase();

    //
    return new Neo4jGraphDb(userObject, temp, 5001);
  }
}
