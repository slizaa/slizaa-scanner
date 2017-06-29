package org.slizaa.scanner.jtype.bytecode.util;

import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.api.model.internal.NodeBean;
import org.slizaa.scanner.jtype.model.internal.primitvedatatypes.IPrimitiveDatatypeNodeProvider;

public class PrimitiveDatatypeNodeProvider implements IPrimitiveDatatypeNodeProvider {

  private INode _primitiveDatatypeByte    = new NodeBean();

  private INode _primitiveDatatypeShort   = new NodeBean();

  private INode _primitiveDatatypeInt     = new NodeBean();

  private INode _primitiveDatatypeLong    = new NodeBean();

  private INode _primitiveDatatypeFloat   = new NodeBean();

  private INode _primitiveDatatypeDouble  = new NodeBean();

  private INode _primitiveDatatypeChar    = new NodeBean();

  private INode _primitiveDatatypeBoolean = new NodeBean();

  private INode _primitiveDatatypeVoid    = new NodeBean();

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
