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
package org.slizaa.scanner.api.model;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IModifiableNode extends INode {

  /**
   * <p>
   * </p>
   * 
   * @param targetBean
   * @param relationshipType
   * @return
   */
  IRelationship addRelationship(INode targetBean, RelationshipType relationshipType);
  
  void clearRelationships();

  /**
   * <p>
   * </p>
   * 
   * @param key
   * @param value
   */
  void putProperty(String key, Object value);

  /**
   * <p>
   * </p>
   * 
   * @param label
   */
  void addLabel(Label label);
  
  void setNodeId(long id);

  boolean containsRelationship(RelationshipType type, INode node);
}
