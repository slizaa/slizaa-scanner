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

import java.util.Map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.GraphDatabaseAPI;

public class Main {

  public static void main(String[] args) {

    //
    GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("D:\\50-Development\\environments\\slizaa-master\\ws\\org.slizaa.scanner\\org.slizaa.scanner.itest\\target\\unittests\\1497434344533SimpleJTypeJDKTest");
    registerShutdownHook(graphDb);
    
    //
      ExecutionEngine engine = new ExecutionEngine( graphDb );

      try ( Transaction ignored = graphDb.beginTx() )
      {
        ExecutionResult result = engine.execute("MATCH(n) return n");
        for (Map<String, Object> map : result) {
          Node node = (Node) map.get("n");
          System.out.println(node);
          for (Label label : node.getLabels()) {
            System.out.println(" - " + label);
          }
          for (String key : node.getPropertyKeys()) {
            System.out.println(" - " + key + " : " + node.getProperty(key));
          }
        }
      }


    //
    graphDb.shutdown();
  }

  private static void registerShutdownHook(final GraphDatabaseService graphDb) {
    // Registers a shutdown hook for the Neo4j instance so that it
    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
    // running application).
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        graphDb.shutdown();
      }
    });
  }
}
