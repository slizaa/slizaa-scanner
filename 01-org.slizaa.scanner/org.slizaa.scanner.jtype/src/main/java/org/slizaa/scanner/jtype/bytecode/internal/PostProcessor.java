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
package org.slizaa.scanner.jtype.bytecode.internal;

import java.util.LinkedList;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.slizaa.scanner.api.model.resource.CoreModelElementType;
import org.slizaa.scanner.jtype.model.JTypeLabel;
import org.slizaa.scanner.jtype.model.JTypeModelRelationshipType;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PostProcessor {

  /**
   * <p>
   * </p>
   * 
   * @param node
   */
  public static void deleteAllJTypeRelatedNodesForResourceNode(final Node node) {

    //
    TraversalDescription traversalDescription = node.getGraphDatabase().traversalDescription()
        .expand(new PathExpander() {
          @Override
          public Iterable<Relationship> expand(Path path, BranchState state) {

            List<Relationship> relationships = new LinkedList<Relationship>();
            for (Relationship relationship : path.endNode().getRelationships(Direction.OUTGOING)) {
              if (!relationship.isType(RelationshipType.withName((JTypeModelRelationshipType.BOUND_TO.name())))) {
                relationships.add(relationship);
              }
            }
            return relationships;
          }

          @Override
          public PathExpander reverse() {
            return this;
          }
        }).evaluator(new Evaluator() {

          @Override
          public Evaluation evaluate(Path path) {

            // exclude 'self'
            if (path.length() == 0) {
              return Evaluation.EXCLUDE_AND_CONTINUE;
            }

            if (path.endNode().hasLabel(Label.label(JTypeLabel.PRIMITIVE_DATA_TYPE.name()))) {
              return Evaluation.EXCLUDE_AND_PRUNE;
            }

            return Evaluation.INCLUDE_AND_CONTINUE;
          }
        });

    //
    Traverser traverser = traversalDescription.traverse(node);

    //
    for (Node visitedNode : traverser.nodes()) {
      if (!visitedNode.hasLabel(Label.label(CoreModelElementType.RESOURCE.name()))) {
        for (Relationship relationship : visitedNode.getRelationships()) {
          relationship.delete();
        }
        visitedNode.delete();
      }
    }
  }

}
