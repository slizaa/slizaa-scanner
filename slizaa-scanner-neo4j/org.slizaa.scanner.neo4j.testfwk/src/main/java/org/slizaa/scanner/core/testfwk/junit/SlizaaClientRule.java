package org.slizaa.scanner.core.testfwk.junit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class SlizaaClientRule implements TestRule {

  private Session _session;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Session getSession() {
    return _session;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Statement apply(Statement base, Description description) {

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {
        //
        try (Driver driver = GraphDatabase.driver("bolt://localhost:5001",
            Config.build().withoutEncryption().toConfig())) {

          _session = driver.session();

          base.evaluate();

          _session = null;
        }
      }
    };
  }
}
