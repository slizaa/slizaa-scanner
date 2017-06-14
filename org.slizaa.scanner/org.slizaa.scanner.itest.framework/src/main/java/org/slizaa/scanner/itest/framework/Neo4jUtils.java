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
package org.slizaa.scanner.itest.framework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class Neo4jUtils {

  public static void countNodes(String label, GraphDatabaseService graphDatabaseService) {

    //
    Transaction tx = graphDatabaseService.beginTx();

    Neo4jUtils.executeWithExecutionEngine(new Neo4jUtils.ExecuteEngineAction() {
      @Override
      public void executeWithExecutionEngine(ExecutionEngine engine) {
        ExecutionResult result = engine.execute("MATCH(n:TYPE) return count(n)");
        for (Map<String, Object> map : result) {
          assertThat(map.entrySet()).hasSize(1);
          System.out.println(map.get("count(n)"));
        }
      }
    }, graphDatabaseService);

    //
    tx.close();
  }

  public static void executeWithExecutionEngine(ExecuteEngineAction action, GraphDatabaseService graphDatabaseService) {

    //
    ExecutionEngine engine = new ExecutionEngine(graphDatabaseService);
    Transaction tx = graphDatabaseService.beginTx();

    action.executeWithExecutionEngine(engine);

    //
    tx.success();
    tx.close();
  }

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   * 
   */
  public static interface ExecuteEngineAction {

    void executeWithExecutionEngine(ExecutionEngine engine);
  }
}
