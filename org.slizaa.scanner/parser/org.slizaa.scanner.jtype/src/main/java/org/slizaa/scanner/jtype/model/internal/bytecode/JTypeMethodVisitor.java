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
package org.slizaa.scanner.jtype.model.internal.bytecode;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.slizaa.scanner.jtype.model.JTypeModelRelationshipType;
import org.slizaa.scanner.model.IModifiableNode;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JTypeMethodVisitor extends MethodVisitor {

  /** - */
  private TypeLocalReferenceCache _typeLocalReferenceCache;

  /** - */
  private IModifiableNode         _methodNodeBean;

  /**
   * <p>
   * </p>
   * 
   * @param recorder
   * @param type
   */
  public JTypeMethodVisitor(IModifiableNode methodNodeBean, TypeLocalReferenceCache typeReferenceHolder) {
    super(Opcodes.ASM5);

    //
    this._methodNodeBean = methodNodeBean;
    this._typeLocalReferenceCache = typeReferenceHolder;
  }

  /**
   * <p>
   * </p>
   * 
   * @inheritDoc
   */
  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

    // TODO!
    _typeLocalReferenceCache.addTypeReference(_methodNodeBean, Type.getType(desc),
        JTypeModelRelationshipType.REFERENCES);

    //
    return null;
  }

  /**
   * @inheritDoc
   */
  public void visitFieldInsn(int opcode, String owner, String name, String desc) {

    //
    String opcode_str = null;

    // opcode
    switch (opcode) {
    case Opcodes.GETSTATIC: {
      opcode_str = "GETSTATIC";
      break;
    }
    case Opcodes.PUTSTATIC: {
      opcode_str = "PUTSTATIC";
      break;
    }
    case Opcodes.GETFIELD: {
      opcode_str = "GETFIELD";
      break;
    }
    case Opcodes.PUTFIELD: {
      opcode_str = "PUTFIELD";
      break;
    }
    default:
      // TODO
      throw new RuntimeException();
    }

    String ownerTypeName = Utils.getFullyQualifiedTypeName(Type.getObjectType(owner));
    String fieldType = Utils.getFullyQualifiedTypeName(Type.getType(desc));

    FieldDescriptor fieldDescriptor = new FieldDescriptor(ownerTypeName, name, fieldType,
        opcode == Opcodes.GETSTATIC || opcode == Opcodes.PUTSTATIC);

    //
    JTypeModelRelationshipType relationshipType = opcode == Opcodes.GETSTATIC || opcode == Opcodes.GETFIELD
        ? JTypeModelRelationshipType.READ : JTypeModelRelationshipType.WRITE;

    _typeLocalReferenceCache.addFieldReference(_methodNodeBean, fieldDescriptor, relationshipType);
  }

  /**
   * @inheritDoc
   */
  @Override
  public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {

    _typeLocalReferenceCache.addTypeReference(_methodNodeBean, Type.getType(desc),
        JTypeModelRelationshipType.REFERENCES);
  }

  /**
   * @inheritDoc
   */
  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String desc) {
    // System.out.println("owner" + owner);
    // System.out.println("name" + name);
    // System.out.println("desc" + desc);

    Type t = Type.getObjectType(owner);
    _typeLocalReferenceCache.addTypeReference(_methodNodeBean, t, JTypeModelRelationshipType.REFERENCES);
    _typeLocalReferenceCache.addTypeReference(_methodNodeBean, Type.getReturnType(desc),
        JTypeModelRelationshipType.REFERENCES);
    _typeLocalReferenceCache.addTypeReference(_methodNodeBean, Type.getArgumentTypes(desc),
        JTypeModelRelationshipType.REFERENCES);
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {

    // if (owner.equals("java/lang/System")) {
    // System.out.println("----------------------");
    // System.out.println(owner);
    // System.out.println(name);
    // System.out.println(desc);
    // }
  }

  /**
   * @inheritDoc
   */
  public void visitMultiANewArrayInsn(String desc, int dims) {

    // Type t = ;
    _typeLocalReferenceCache.addTypeReference(_methodNodeBean, Type.getType(desc),
        JTypeModelRelationshipType.REFERENCES);
  }

  /**
   * @inheritDoc
   */
  public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {

    _typeLocalReferenceCache.addTypeReference(_methodNodeBean, Type.getType(desc),
        JTypeModelRelationshipType.REFERENCES);
    return null;
  }

  /**
   * @inheritDoc
   */
  public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
    if (type != null) {
      _typeLocalReferenceCache.addTypeReference(_methodNodeBean, Type.getObjectType(type),
          JTypeModelRelationshipType.REFERENCES);
    }
  }

  /**
   * @inheritDoc
   */
  public void visitTypeInsn(int opcode, String type) {

    if (opcode != Opcodes.CHECKCAST && opcode != Opcodes.NEW && opcode != Opcodes.INSTANCEOF
        && opcode != Opcodes.ANEWARRAY) {

    }

    _typeLocalReferenceCache.addTypeReference(_methodNodeBean, Type.getObjectType(type),
        JTypeModelRelationshipType.REFERENCES);
  }

  /**
   * {@inheritDoc}
   */
  public void visitLdcInsn(Object cst) {
    // TODO
    if (cst instanceof Type) {
      _typeLocalReferenceCache.addTypeReference(_methodNodeBean, (Type) cst, JTypeModelRelationshipType.REFERENCES);
    }
  }

  @Override
  public void visitParameter(String name, int access) {
    super.visitParameter(name, access);
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
    // TODO Auto-generated method stub
    return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
  }

  @Override
  public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
    // TODO Auto-generated method stub
    return super.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
  }
}
