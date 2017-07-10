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
package org.slizaa.scanner.jtype.model.internal.primitvedatatypes;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.api.model.NodeFactory;
import org.slizaa.scanner.jtype.model.JTypeLabel;

public class PrimitiveDatatypeNodeProvider implements IPrimitiveDatatypeNodeProvider {

  /** - */
  private INode _primitiveDatatypeByte;

  /** - */
  private INode _primitiveDatatypeShort;

  /** - */
  private INode _primitiveDatatypeInt;

  /** - */
  private INode _primitiveDatatypeLong;

  /** - */
  private INode _primitiveDatatypeFloat;

  /** - */
  private INode _primitiveDatatypeDouble;

  /** - */
  private INode _primitiveDatatypeChar;

  /** - */
  private INode _primitiveDatatypeBoolean;

  /** - */
  private INode _void;

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeByte() {
    return _primitiveDatatypeByte;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeShort() {
    return _primitiveDatatypeShort;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeInt() {
    return _primitiveDatatypeInt;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeLong() {
    return _primitiveDatatypeLong;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeFloat() {
    return _primitiveDatatypeFloat;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeDouble() {
    return _primitiveDatatypeDouble;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeChar() {
    return _primitiveDatatypeChar;
  }

  /**
   * {@inheritDoc}
   */
  public INode getPrimitiveDatatypeBoolean() {
    return _primitiveDatatypeBoolean;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public INode getVoid() {
    return _void;
  }

  /**
   * <p>
   * Creates a new instance of type {@link PrimitiveDatatypeNodeProvider}.
   * </p>
   * 
   * @param graphDatabase
   */
  public PrimitiveDatatypeNodeProvider(GraphDatabaseService graphDatabase) {

    // we have to create nodes for the primitive data types (byte, short, int etc.) if they don't already exist
    _primitiveDatatypeByte = createPrimitveDataTypeIfNotExists(graphDatabase, "byte",
        JTypeLabel.PRIMITIVE_DATA_TYPE);
    _primitiveDatatypeShort = createPrimitveDataTypeIfNotExists(graphDatabase, "short",
        JTypeLabel.PRIMITIVE_DATA_TYPE);
    _primitiveDatatypeInt = createPrimitveDataTypeIfNotExists(graphDatabase, "int",
        JTypeLabel.PRIMITIVE_DATA_TYPE);
    _primitiveDatatypeLong = createPrimitveDataTypeIfNotExists(graphDatabase, "long",
        JTypeLabel.PRIMITIVE_DATA_TYPE);
    _primitiveDatatypeFloat = createPrimitveDataTypeIfNotExists(graphDatabase, "float",
        JTypeLabel.PRIMITIVE_DATA_TYPE);
    _primitiveDatatypeDouble = createPrimitveDataTypeIfNotExists(graphDatabase, "double",
        JTypeLabel.PRIMITIVE_DATA_TYPE);
    _primitiveDatatypeChar = createPrimitveDataTypeIfNotExists(graphDatabase, "char",
        JTypeLabel.PRIMITIVE_DATA_TYPE);
    _primitiveDatatypeBoolean = createPrimitveDataTypeIfNotExists(graphDatabase, "boolean",
        JTypeLabel.PRIMITIVE_DATA_TYPE);

    // void
    _void = createPrimitveDataTypeIfNotExists(graphDatabase, "void", JTypeLabel.VOID);
  }

  /**
   * <p>
   * Tests if the database contains a node with the label {@link JTypeLabel#PRIMITIVE_DATA_TYPE} that
   * represents the specified primitive data type). If the node does not exist, a new node will be created.
   * </p>
   * 
   * @param graphDatabase
   * @param primtiveDataType
   * @return
   */
  private IModifiableNode createPrimitveDataTypeIfNotExists(GraphDatabaseService graphDatabase, String primtiveDataType,
      JTypeLabel typeType) {

    //
    Label label = Label.label(typeType.name());

    //
    ResourceIterator<Node> nodes = graphDatabase.findNodes(label, "fqn", primtiveDataType);

    //
    Node node = null;

    //
    if (!nodes.hasNext()) {
      node = graphDatabase.createNode(label);
      node.setProperty("fqn", primtiveDataType);
    } else {
      node = nodes.next();
    }

    //
    IModifiableNode nodeBean = NodeFactory.createNode(node.getId());
    nodeBean.addLabel(typeType);
    nodeBean.putProperty("fqn", primtiveDataType);

    //
    return nodeBean;
  }
}
