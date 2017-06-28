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
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.signature.SignatureReader;
import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.api.model.IRelationship;
import org.slizaa.scanner.api.model.NodeFactory;
import org.slizaa.scanner.api.model.resource.CoreModelRelationshipType;
import org.slizaa.scanner.jtype.model.AccessLevel;
import org.slizaa.scanner.jtype.model.IFieldNode;
import org.slizaa.scanner.jtype.model.IMethodNode;
import org.slizaa.scanner.jtype.model.ITypeNode;
import org.slizaa.scanner.jtype.model.JTypeModelElementType;
import org.slizaa.scanner.jtype.model.JTypeModelRelationshipType;
import org.slizaa.scanner.jtype.model.JavaTypeUtils;
import org.slizaa.scanner.jtype.model.TypeType;
import org.slizaa.scanner.jtype.model.internal.primitvedatatypes.IPrimitiveDatatypeNodeProvider;

/**
 */
public class JTypeClassVisitor extends ClassVisitor {

  // TODO
  private boolean                        _analyzeReferences = true;

  /** - */
  private IModifiableNode                _typeBean;

  /** - */
  private TypeLocalReferenceCache        _classLocalTypeReferenceCache;

  /** - */
  private IPrimitiveDatatypeNodeProvider _primitiveDatatypeNodes;

  /**
   * <p>
   * Creates a new instance of type {@link JTypeClassVisitor}.
   * </p>
   * 
   * @param batchInserter
   */
  public JTypeClassVisitor(IPrimitiveDatatypeNodeProvider datatypeNodeProvider) {
    super(Opcodes.ASM5);

    //
    _primitiveDatatypeNodes = datatypeNodeProvider;
    _classLocalTypeReferenceCache = new TypeLocalReferenceCache(_primitiveDatatypeNodes);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public IModifiableNode getTypeBean() {
    return _typeBean;
  }

  @Override
  public void visit(final int version, final int access, final String name, final String signature,
      final String superName, final String[] interfaces) {

    // TODO: get the NodeBean from the cache
    _typeBean = NodeFactory.createNode();
    _typeBean.addLabel(JTypeModelElementType.TYPE);

    // add the type of the type
    switch (TypeType.getTypeType(access)) {
    case ANNOTATION: {
      _typeBean.addLabel(TypeType.ANNOTATION);
      _typeBean.putProperty(ITypeNode.NODETYPE, TypeType.ANNOTATION.name());
      break;
    }
    case CLASS: {
      _typeBean.addLabel(TypeType.CLASS);
      _typeBean.putProperty(ITypeNode.NODETYPE, TypeType.CLASS.name());
      break;
    }
    case ENUM: {
      _typeBean.addLabel(TypeType.ENUM);
      _typeBean.putProperty(ITypeNode.NODETYPE, TypeType.ENUM.name());
      break;
    }
    case INTERFACE: {
      _typeBean.addLabel(TypeType.INTERFACE);
      _typeBean.putProperty(ITypeNode.NODETYPE, TypeType.INTERFACE.name());
      break;
    }
    }

    // class name
    _typeBean.putProperty(ITypeNode.FQN, name.replace('/', '.'));
    _typeBean.putProperty(ITypeNode.NAME, JavaTypeUtils.getSimpleName(name.replace('/', '.')));

    // class version
    int major = version & 0xFFFF;
    int minor = version >>> 16;
    _typeBean.putProperty(ITypeNode.CLASS_VERSION, String.format("%s.%s (%s)", major, minor, version));

    // deprecated
    if ((access & Opcodes.ACC_DEPRECATED) == Opcodes.ACC_DEPRECATED) {
      _typeBean.putProperty(ITypeNode.DEPRECATED, true);
    }

    // access flags
    _typeBean.putProperty(ITypeNode.ACCESS_FLAGS, Integer.toHexString(access).toUpperCase());

    //
    if ((access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT) {
      _typeBean.putProperty(ITypeNode.ABSTRACT, true);
    }

    //
    if ((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC) {
      _typeBean.putProperty(ITypeNode.STATIC, true);
    }

    //
    if ((access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL) {
      _typeBean.putProperty(ITypeNode.FINAL, true);
    }

    //
    if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
      _typeBean.putProperty(ITypeNode.ACCESS_LEVEL, AccessLevel.PUBLIC.name());
    }
    //
    else {
      _typeBean.putProperty(ITypeNode.ACCESS_LEVEL, AccessLevel.PACKAGE_PRIVATE.name());
    }

    // TODO!!
    if (signature != null) {

      // set signature
      _typeBean.putProperty(ITypeNode.SIGNATURE, signature);

      JTypeSignatureVisitor sv = new JTypeSignatureVisitor(access);
      SignatureReader r = new SignatureReader(signature);
      r.accept(sv);

      // String declaration = name + sv.getDeclaration();
      // System.out.println("declaration " + declaration);
    }

    // add 'extends' references
    _classLocalTypeReferenceCache.addTypeReference(_typeBean, superName, JTypeModelRelationshipType.EXTENDS);

    // add 'implements' references
    for (String ifaceName : interfaces) {

      switch (TypeType.getTypeType(access)) {
      case CLASS:
        _classLocalTypeReferenceCache.addTypeReference(_typeBean, ifaceName, JTypeModelRelationshipType.IMPLEMENTS);
        break;
      case INTERFACE:
        _classLocalTypeReferenceCache.addTypeReference(_typeBean, ifaceName, JTypeModelRelationshipType.EXTENDS);
        break;
      case ENUM:
        break;
      case ANNOTATION:
        break;
      }
    }
  }

  /**
   * @inheritDoc
   */
  @Override
  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

    //
    if (!_analyzeReferences) {
      return null;
    }

    // class annotation
    _classLocalTypeReferenceCache.addTypeReference(_typeBean, Type.getType(desc),
        JTypeModelRelationshipType.REFERENCES);

    //
    return null;
  }

  /**
   * @inheritDoc
   */
  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

    // create and add new method bean
    IModifiableNode methodBean = NodeFactory.createNode();
    _typeBean.addRelationship(methodBean, CoreModelRelationshipType.CONTAINS);

    // set labels and 'nodetype' property
    methodBean.putProperty(INode.NODETYPE, JTypeModelElementType.METHOD.name());
    methodBean.addLabel(JTypeModelElementType.METHOD);

    // add method name
    methodBean.putProperty(IMethodNode.NAME, name);
    methodBean.putProperty(IMethodNode.FQN, Utils.getMethodSignature(name, desc));

    // signature
    if (signature != null) {
      methodBean.putProperty("signature", signature);
    }

    //
    methodBean.putProperty(IMethodNode.NATIVE, (access & Opcodes.ACC_NATIVE) == Opcodes.ACC_NATIVE);
    methodBean.putProperty(IMethodNode.ABSTRACT, (access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT);
    methodBean.putProperty(IMethodNode.SYNTHETIC, (access & Opcodes.ACC_SYNTHETIC) == Opcodes.ACC_SYNTHETIC);
    methodBean.putProperty(IMethodNode.STATIC, (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC);
    methodBean.putProperty(IMethodNode.FINAL, (access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL);

    // Access modifiers: public, protected, and private
    if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
      methodBean.putProperty(IMethodNode.ACCESS_LEVEL, AccessLevel.PUBLIC.name());
    } else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
      methodBean.putProperty(IMethodNode.ACCESS_LEVEL, AccessLevel.PROTECTED.name());
    } else if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
      methodBean.putProperty(IMethodNode.ACCESS_LEVEL, AccessLevel.PRIVATE.name());
    } else {
      methodBean.putProperty(IMethodNode.ACCESS_LEVEL, AccessLevel.PACKAGE_PRIVATE.name());
    }

    // arguments
    Type[] types = Type.getArgumentTypes(desc);
    for (int i = 0; i < types.length; i++) {
      IRelationship relationship = addReference(methodBean, types[i], JTypeModelRelationshipType.HAS_PARAMETER);
      relationship.putRelationshipProperty(IMethodNode.PARAMETER_INDEX, i);
    }

    // return type
    addReference(methodBean, Type.getReturnType(desc), JTypeModelRelationshipType.RETURNS);

    // exceptions
    if (exceptions != null) {
      for (String exception : exceptions) {
        addReference(methodBean, Type.getObjectType(exception), JTypeModelRelationshipType.THROWS);
      }
    }

    //
    return new JTypeMethodVisitor(methodBean, _classLocalTypeReferenceCache);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FieldVisitor visitField(int access, String name, String desc, String signature, final Object value) {

    // create bean and add it to the type bean
    IModifiableNode fieldBean = NodeFactory.createNode();
    fieldBean.addLabel(JTypeModelElementType.FIELD);
    _typeBean.addRelationship(fieldBean, CoreModelRelationshipType.CONTAINS);

    // add field name
    fieldBean.putProperty(INode.NODETYPE, JTypeModelElementType.FIELD.name());
    // TODO!!
    // fieldBean.putProperty(IMethodNode.FQN, Utils.getFieldSignature(name, desc));

    // get the type
    addReference(fieldBean, Type.getType(desc), JTypeModelRelationshipType.IS_OF_TYPE);

    //
    fieldBean.putProperty(IFieldNode.NAME, name);

    // access flags
    fieldBean.putProperty(IFieldNode.ACCESS_FLAGS, Integer.toHexString(access).toUpperCase());

    // Access modifiers: public, protected, and private
    if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
      fieldBean.putProperty(IFieldNode.ACCESS_LEVEL, AccessLevel.PUBLIC.name());
    } else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
      fieldBean.putProperty(IFieldNode.ACCESS_LEVEL, AccessLevel.PROTECTED.name());
    } else if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
      fieldBean.putProperty(IFieldNode.ACCESS_LEVEL, AccessLevel.PRIVATE.name());
    } else {
      fieldBean.putProperty(IFieldNode.ACCESS_LEVEL, AccessLevel.PACKAGE_PRIVATE.name());
    }

    // Field-specific modifiers governing runtime behavior: transient and volatile
    fieldBean.putProperty(IFieldNode.TRANSIENT, (access & Opcodes.ACC_TRANSIENT) == Opcodes.ACC_TRANSIENT);
    fieldBean.putProperty(IFieldNode.VOLATILE, (access & Opcodes.ACC_VOLATILE) == Opcodes.ACC_VOLATILE);

    // Modifier restricting to one instance: static
    fieldBean.putProperty(IFieldNode.STATIC, (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC);

    // Modifier prohibiting value modification: final
    fieldBean.putProperty(IFieldNode.FINAL, (access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL);

    // deprecation
    fieldBean.putProperty(IFieldNode.DEPRECATED, (access & Opcodes.ACC_DEPRECATED) == Opcodes.ACC_DEPRECATED);

    //
    if (signature != null) {

      // signature
      fieldBean.putProperty(IFieldNode.SIGNATURE, signature);

      // TODO
      JTypeSignatureVisitor sv = new JTypeSignatureVisitor(0);
      SignatureReader r = new SignatureReader(signature);
      r.acceptType(sv);
    }

    // TODO
    return new JTypeFieldVisitor(this);
  }

  @Override
  public void visitSource(String source, String debug) {
    // TODO Auto-generated method stub
    super.visitSource(source, debug);
  }

  @Override
  public void visitOuterClass(String owner, String name, String desc) {
    // TODO Auto-generated method stub
    super.visitOuterClass(owner, name, desc);
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
    // TODO Auto-generated method stub
    return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
  }

  @Override
  public void visitAttribute(Attribute attr) {
    // TODO Auto-generated method stub
    super.visitAttribute(attr);
  }

  @Override
  public void visitInnerClass(String name, String outerName, String innerName, int access) {
    // http://stackoverflow.com/questions/24622658/access-flag-for-private-inner-classes-in-java-spec-inconsistent-with-reflectio

    //
    if (name.replace('/', '.').equals(_typeBean.getFullyQualifiedName())) {

      _typeBean.putProperty(ITypeNode.INNER_CLASS, true);

      // outer name
      if (outerName != null) {
        _typeBean.putProperty(ITypeNode.OUTER_CLASSNAME, outerName.replace('/', '.'));
      }

      // access flags
      if (access != 0) {

        _typeBean.putProperty(ITypeNode.INNER_CLASS_ACCESS_FLAGS, Integer.toHexString(access).toUpperCase());

        //
        if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
          _typeBean.putProperty(ITypeNode.INNER_CLASS_ACCESS_LEVEL, AccessLevel.PUBLIC.name());
        }
        //
        else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
          _typeBean.putProperty(ITypeNode.INNER_CLASS_ACCESS_LEVEL, AccessLevel.PROTECTED.name());
        }
        //
        else if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
          _typeBean.putProperty(ITypeNode.INNER_CLASS_ACCESS_LEVEL, AccessLevel.PRIVATE.name());
        }
        //
        else {
          _typeBean.putProperty(ITypeNode.INNER_CLASS_ACCESS_LEVEL, AccessLevel.PACKAGE_PRIVATE.name());
        }
      }
    }

    super.visitInnerClass(name, outerName, innerName, access);
  }

  @Override
  public void visitEnd() {
    // TODO Auto-generated method stub
    super.visitEnd();
  }

  /**
   * <p>
   * </p>
   * 
   * @param fieldBean
   * @param type
   */
  private IRelationship addReference(IModifiableNode fieldBean, Type type,
      JTypeModelRelationshipType relationshipType) {

    //
    Type t = Utils.resolveArrayType(type);

    //
    if (Utils.isVoid(type)) {
      return null;
    } else if (Utils.isPrimitive(t)) {
      return fieldBean.addRelationship(Utils.getPrimitiveDatatypeNode(t, _primitiveDatatypeNodes), relationshipType);
    } else {
      return _classLocalTypeReferenceCache.addTypeReference(fieldBean, t.getClassName(), relationshipType);
    }
  }
}
