package org.slizaa.scanner.eclipse.itest;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Config.EncryptionLevel;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.osgi.framework.BundleException;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GraphDbFactoryTest extends AbstractEclipseTest {

  /** - */
  @Inject
  private IGraphDbFactory graphDbFactory;

  /**
   * <p>
   * </p>
   * 
   * @throws BundleException
   */
  @Test
  @Ignore
  public void testDatabaseAndDriver() throws BundleException {

    // TODO: TEMP DIR
    IGraphDb graphDb = graphDbFactory.createGraphDb(5001, new File("C:\\_schnurz"), null);
    assertThat(graphDb).isNotNull();

    //
    Config config = Config.build().withEncryptionLevel(EncryptionLevel.NONE).toConfig();
    Driver driver = GraphDatabase.driver("bolt://localhost:5001", config);
    assertThat(driver).isNotNull();

    //
    try (Session session = driver.session()) {
      // TODO
//      StatementResult result = session.run("return slizaa.currentTimestamp()");
//      assertThat(result.next().get("slizaa.currentTimestamp()")).isNotNull();
    }
    
    //
    graphDb.shutdown();
  }
}