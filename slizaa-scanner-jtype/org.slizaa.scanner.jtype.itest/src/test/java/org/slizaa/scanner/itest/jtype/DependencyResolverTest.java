/*******************************************************************************
 * Copyright (c) 2011-2015 Slizaa project team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Slizaa project team - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.itest.jtype;

import static org.slizaa.scanner.core.testfwk.junit.ContentDefinitionsUtils.multipleBinaryMvnArtifacts;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import org.slizaa.scanner.core.testfwk.junit.SlizaaClientRule;
import org.slizaa.scanner.core.testfwk.junit.SlizaaTestServerRule;

public class DependencyResolverTest {

  @ClassRule
  public static SlizaaTestServerRule _server = new SlizaaTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "com.netflix.eureka", "eureka-core", "1.8.2" },
          new String[] { "com.netflix.eureka", "eureka-client", "1.8.2" }));

  @Rule
  public SlizaaClientRule            _client = new SlizaaClientRule();

  /**
   * <p>
   * </p>
   */
  @Test
  public void testDependencyResolver() {

    StatementResult statementResult = _client.getSession().run(
        "MATCH (node)-[:INVOKES]->(mref:METHOD_REFERENCE)-[IS_DEFINED_BY]->(tref:TYPE_REFERENCE) MATCH (t:TYPE)-[:CONTAINS]->(m:METHOD) WHERE tref.fqn = t.fqn AND mref.fqn = m.fqn CREATE (mref)-[:RESOLVES_TO {derived:true}]->(m) CREATE (node)-[:INVOKES {derived:true}]->(m)");
    statementResult.forEachRemaining(c -> System.out.println(c.fields()));

    statementResult = _client.getSession().run(
        "MATCH (m1:METHOD)-[:INVOKES]->(m2:METHOD) RETURN m1, m2");
    statementResult.forEachRemaining(c -> System.out.println(c.fields()));
  }
}
