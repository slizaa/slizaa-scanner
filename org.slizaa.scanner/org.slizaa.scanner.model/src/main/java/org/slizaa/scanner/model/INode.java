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
package org.slizaa.scanner.model;

import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

/**
 * <p>
 * Represents and encapsulates a node in the underlying graph database model.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface INode {

  /** the key for the attribute 'nodetype' */
  public static final String NODETYPE = "nodetype";

  /** the key for the attribute 'fqn' */
  public static final String FQN      = "fqn";

  /** the key for the attribute 'name' */
  public static final String NAME     = "name";

  /**
   * <p>
   * Returns the internal id of the underlying database node.
   * </p>
   * 
   * @return the internal id of the underlying database node.
   */
  long getId();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  String getNodetype();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  String getFullyQualifiedName();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  String getName();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  Map<String, Object> getProperties();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  List<Label> getLabels();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  Map<RelationshipType, List<IRelationship>> getRelationships();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  boolean hasNodeId();
}
