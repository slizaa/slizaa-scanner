/*******************************************************************************
 * Copyright (C) 2017 wuetherich
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.neo4j.testfwk;

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
