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
package org.slizaa.scanner.jtype.model.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.slizaa.scanner.jtype.model.JTypeModelRelationshipType;

import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class Linker {

  public void link(GraphDatabaseService db, IProgressMonitor progressMonitor) {

    Stopwatch stopWatch = Stopwatch.createStarted();

    ExecutionEngine executionEngine = new ExecutionEngine(db);
    try (Transaction transaction = db.beginTx()) {

      // create type cache
      final LoadingCache<String, List<Node>> typeNodesCache = CacheBuilder.newBuilder().build(
          new CacheLoader<String, List<Node>>() {
            public List<Node> load(String key) {
              return new ArrayList<>();
            }
          });

      //
      String query;
      ExecutionResult result;
      ResourceIterator<Map<String, Object>> iterator;

      //
      System.out.println("Building cache..." + stopWatch.elapsed(TimeUnit.MILLISECONDS));

      //
      query = "MATCH (t:TYPE) RETURN t, t.fqn";
      result = executionEngine.execute(query);
      iterator = result.iterator();
      while (iterator.hasNext()) {
        Map<String, Object> map = iterator.next();
        typeNodesCache.getUnchecked((String) map.get("t.fqn")).add((Node) map.get("t"));
      }

      //
      System.out.println("resolving references..." + stopWatch.elapsed(TimeUnit.MILLISECONDS));

      //
      // query =
      // "MATCH (t:TYPE_REFERENCE) WHERE NOT (t)-[:BOUND_TO]->() AND NOT t.fqn IN [\"void\",\"int\",\"long\",\"double\",\"boolean\",\"char\"] RETURN t, t.fqn";
      // result = executionEngine.execute(query);
      // iterator = result.iterator();
      // while (iterator.hasNext()) {
      // Map<String, Object> map = iterator.next();
      // String ref_fqn = (String) map.get("t.fqn");
      // if (typeNodesCache.asMap().containsKey(ref_fqn)) {
      // Node node = (Node) map.get("t");
      //
      // node.createRelationshipTo(_typeNode(typeNodesCache, ref_fqn), JTypeModelRelationshipType.BOUND_TO);
      // }
      // }

      //
      query = "MATCH (t:TYPE_REFERENCE) WHERE NOT (t)-[:BOUND_TO]->() RETURN t, t.fqn";
      result = executionEngine.execute(query);
      iterator = result.iterator();
      System.out.println("Before count...");
      int count = IteratorUtil.count(iterator);
      progressMonitor.beginTask("RESOLVING", count);
      System.out.println("After count...");
      result = executionEngine.execute(query);
      iterator = result.iterator();
      while (iterator.hasNext()) {
        Map<String, Object> map = iterator.next();
        String ref_fqn = (String) map.get("t.fqn");
        // System.out.println(ref_fqn);
        if (typeNodesCache.asMap().containsKey(ref_fqn)) {
          Node node = (Node) map.get("t");
          node.createRelationshipTo(typeNodesCache.asMap().get(ref_fqn).get(0), JTypeModelRelationshipType.BOUND_TO);
        }
        progressMonitor.worked(1);
      }

      transaction.success();
    }

    stopWatch.stop();
    System.out.println("Stopwatch " + stopWatch.elapsed(TimeUnit.MILLISECONDS));

    /**************************/

    // // create type cache
    // final LoadingCache<String, List<Node>> typeNodesCache = CacheBuilder.newBuilder().build(
    // new CacheLoader<String, List<Node>>() {
    // public List<Node> load(String key) {
    // return new ArrayList<>();
    // }
    // });
    //
    // // cache all type nodes
    // for (Node typeNode : GlobalGraphOperations.at(graphDatabase).getAllNodesWithLabel(JTypeModelElementType.TYPE)) {
    // typeNodesCache.getUnchecked((String) typeNode.getProperty("fqn")).add(typeNode);
    // }
    //
    // /****/
    // // http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-5.html#jvms-5.4
    // // associate type references (TYPE_REFERENCE -BOUND_TO-> TYPE)
    // for (Node typeReferenceNode : GlobalGraphOperations.at(graphDatabase).getAllNodesWithLabel(
    // JTypeModelElementType.TYPE_REFERENCE)) {
    //
    // // get the fullyQualifiedName
    // String fqn = (String) typeReferenceNode.getProperty("fqn");
    //
    // //
    // Node typeNode = _typeNode(typeNodesCache, fqn);
    // if (typeNode != null) {
    // typeReferenceNode.createRelationshipTo(typeNode, JTypeModelRelationshipType.BOUND_TO);
    // }
    // }
    //
    // /****/
    // // associate field references (FIELD_REFERENCE -BOUND_TO-> FIELD)
    // // http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-5.html#jvms-5.4
    // for (Node fieldReferenceNode : GlobalGraphOperations.at(graphDatabase).getAllNodesWithLabel(
    // JTypeModelElementType.FIELD_REFERENCE)) {
    //
    // // get the fullyQualifiedName
    // String ownerTypeName = (String) fieldReferenceNode.getProperty(IFieldReferenceNode.OWNER_TYPE_NAME);
    // String fieldName = (String) fieldReferenceNode.getProperty(IFieldReferenceNode.NAME);
    //
    // //
    // Node typeNode = _typeNode(typeNodesCache, ownerTypeName);
    // if (typeNode != null) {
    //
    // for (Relationship relationship : typeNode.getRelationships(CoreModelRelationshipType.CONTAINS,
    // Direction.OUTGOING)) {
    //
    // //
    // Node fieldNode = relationship.getEndNode();
    //
    // if (fieldNode.hasLabel(JTypeModelElementType.FIELD)
    // && fieldNode.getProperty(IFieldNode.NAME, "<undefined>").equals(fieldName)) {
    //
    // //
    // fieldReferenceNode.createRelationshipTo(fieldNode, JTypeModelRelationshipType.BOUND_TO);
    // }
    // }
    // }
    // }
  }

  /**
   * <p>
   * </p>
   * 
   * @param typeNodesCache
   * @param fullyQualifiedName
   * @return
   */
  private Node _typeNode(LoadingCache<String, List<Node>> typeNodesCache, String fullyQualifiedName) {

    // TODO: strategy if multiple types implements a type reference
    if (typeNodesCache.asMap().containsKey(fullyQualifiedName)) {
      return typeNodesCache.getUnchecked(fullyQualifiedName).get(0);
    }

    return null;
  }
}
