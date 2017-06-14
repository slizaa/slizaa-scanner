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

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class Neo4jUtils {

  public static void countNodes(String label, GraphDatabaseService graphDatabaseService) {

    //
    Transaction tx = graphDatabaseService.beginTx();

    Result result = graphDatabaseService.execute("MATCH(n:TYPE) return count(n)");

    while (result.hasNext()) {
      Map<String, Object> map = (Map<String, Object>) result.next();

      assertThat(map.entrySet()).hasSize(1);
      System.out.println(map.get("count(n)"));
    }

    //
    tx.success();
    tx.close();
  }
}
