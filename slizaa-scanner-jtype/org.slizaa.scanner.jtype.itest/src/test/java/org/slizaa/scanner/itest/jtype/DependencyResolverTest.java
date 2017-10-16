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

import static org.slizaa.scanner.neo4j.testfwk.junit.ContentDefinitionsUtils.multipleBinaryMvnArtifacts;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import org.slizaa.scanner.neo4j.testfwk.junit.SlizaaClientRule;
import org.slizaa.scanner.neo4j.testfwk.junit.SlizaaTestServerRule;

import com.google.common.base.Stopwatch;

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

    Stopwatch stopwatch = Stopwatch.createStarted();

    StatementResult statementResult = _client.getSession()
        .run("MATCH (node)-[:INVOKES]->(mref:METHOD_REFERENCE) MATCH (m:METHOD) " + "WHERE "
    // + "NOT (mref)-[:RESOLVES_TO]->(m) "
    // + "AND "
    // + "tref.fqn = t.fqn "
            + "mref.fqn = m.fqn " + "CREATE (mref)-[:RESOLVES_TO {derived:true}]->(m) CREATE (node)-[:INVOKES {derived:true}]->(m)");
    statementResult.consume();

    System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));

    // statementResult = _client.getSession()
    // .run("MATCH p=(mref:METHOD_REFERENCE)-[:RESOLVES_TO]->(m:METHOD) RETURN count(p)");
    // statementResult.forEachRemaining(c -> System.out.println(c.fields()));
    //
    // statementResult = _client.getSession()
    // .run("MATCH (mref:METHOD_REFERENCE) WHERE NOT (mref)-[:RESOLVES_TO]->(:METHOD) RETURN mref.fqn");
    // statementResult.forEachRemaining(c -> System.out.println(c.fields()));
  }
}
