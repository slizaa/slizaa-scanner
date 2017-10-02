package org.slizaa.scanner.jtype.bytecode.util;

import org.slizaa.scanner.core.spi.parser.model.INode;
import org.slizaa.scanner.core.spi.parser.model.NodeFactory;
import org.slizaa.scanner.jtype.bytecode.IPrimitiveDatatypeNodeProvider;

public class PrimitiveDatatypeNodeProvider implements IPrimitiveDatatypeNodeProvider {

  private INode _primitiveDatatypeByte    = NodeFactory.createNode();

  private INode _primitiveDatatypeShort   = NodeFactory.createNode();

  private INode _primitiveDatatypeInt     = NodeFactory.createNode();

  private INode _primitiveDatatypeLong    = NodeFactory.createNode();

  private INode _primitiveDatatypeFloat   = NodeFactory.createNode();

  private INode _primitiveDatatypeDouble  = NodeFactory.createNode();

  private INode _primitiveDatatypeChar    = NodeFactory.createNode();

  private INode _primitiveDatatypeBoolean = NodeFactory.createNode();

  private INode _primitiveDatatypeVoid    = NodeFactory.createNode();

  @Override
  public INode getPrimitiveDatatypeByte() {
    return _primitiveDatatypeByte;
  }

  @Override
  public INode getPrimitiveDatatypeShort() {
    return _primitiveDatatypeShort;
  }

  @Override
  public INode getPrimitiveDatatypeInt() {
    return _primitiveDatatypeInt;
  }

  @Override
  public INode getPrimitiveDatatypeLong() {
    return _primitiveDatatypeLong;
  }

  @Override
  public INode getPrimitiveDatatypeFloat() {
    return _primitiveDatatypeFloat;
  }

  @Override
  public INode getPrimitiveDatatypeDouble() {
    return _primitiveDatatypeDouble;
  }

  @Override
  public INode getPrimitiveDatatypeChar() {
    return _primitiveDatatypeChar;
  }

  @Override
  public INode getPrimitiveDatatypeBoolean() {
    return _primitiveDatatypeBoolean;
  }

  @Override
  public INode getVoid() {
    return _primitiveDatatypeVoid;
  }
}
