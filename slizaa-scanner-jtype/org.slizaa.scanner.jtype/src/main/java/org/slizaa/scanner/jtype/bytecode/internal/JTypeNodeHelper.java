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

import org.objectweb.asm.Type;
import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.api.model.NodeFactory;
import org.slizaa.scanner.jtype.model.IFieldReferenceNode;
import org.slizaa.scanner.jtype.model.IMethodNode;
import org.slizaa.scanner.jtype.model.IMethodReferenceNode;
import org.slizaa.scanner.jtype.model.ITypeReferenceNode;
import org.slizaa.scanner.jtype.model.JTypeLabel;

public class JTypeNodeHelper {

  /**
   * <p>
   * </p>
   * 
   * @param batchInserter
   * @param type
   * @return
   */
  public static IModifiableNode createTypeReferenceNode(final Type type) {

    //
    if (type == null) {
      return null;
    }

    //
    String fqn = Utils.getFullyQualifiedTypeName(type);

    //
    if (fqn != null) {
      return createTypeReferenceNode(fqn);
    }

    //
    return null;
  }

  /**
   * <p>
   * </p>
   * 
   * @param batchInserter
   * @param fullyQualifiedName
   * @return
   */
  public static IModifiableNode createTypeReferenceNode(final String fullyQualifiedName) {

    IModifiableNode node = NodeFactory.createNode();
    node.addLabel(JTypeLabel.TYPE_REFERENCE);
    node.putProperty(ITypeReferenceNode.FQN, fullyQualifiedName.replace('/', '.'));
    return node;
  }

  /**
   * <p>
   * </p>
   * 
   * @param fieldDescriptor
   * @return
   */
  public static IModifiableNode createFieldReferenceNode(final FieldReferenceDescriptor fieldDescriptor) {

    //
    IModifiableNode node = NodeFactory.createNode();
    node.addLabel(JTypeLabel.FIELD_REFERENCE);
    node.putProperty(IFieldReferenceNode.OWNER_TYPE_NAME, fieldDescriptor.getOwnerTypeName().replace('/', '.'));
    node.putProperty(IFieldReferenceNode.NAME, fieldDescriptor.getFieldName());
    node.putProperty(IFieldReferenceNode.TYPE, fieldDescriptor.getFieldType());

    //
    if (fieldDescriptor.isStatic()) {
      node.putProperty(IFieldReferenceNode.STATIC, true);
    }

    //
    return node;
  }

  public static IModifiableNode createMethodReferenceNode(MethodReferenceDescriptor referencedMethod) {

    //
    IModifiableNode node = NodeFactory.createNode();
    node.addLabel(JTypeLabel.METHOD_REFERENCE);
    node.putProperty(IMethodReferenceNode.OWNER_TYPE_NAME, referencedMethod.getOwnerTypeName().replace('/', '.'));
    node.putProperty(IMethodReferenceNode.NAME, referencedMethod.getMethodName());
    node.putProperty(IMethodReferenceNode.SIGNATURE, referencedMethod.getMethodSignature());
    node.putProperty(IMethodReferenceNode.IS_INTERFACE, referencedMethod.getMethodSignature());

    //
    return node;
  }

  /**
   * Checks whether the value contains the flag.
   * 
   * @param value
   *          the value
   * @param flag
   *          the flag
   * @return <code>true</code> if (value & flag) == flag, otherwise <code>false</code>.
   */
  public static boolean hasFlag(int value, int flag) {
    return (value & flag) == flag;
  }
}
