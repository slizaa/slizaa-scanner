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

import static com.google.common.base.Preconditions.checkNotNull;

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
import org.slizaa.scanner.api.model.IRelationship;
import org.slizaa.scanner.api.model.NodeFactory;
import org.slizaa.scanner.api.model.resource.CoreModelRelationshipType;
import org.slizaa.scanner.jtype.bytecode.JTypeByteCodeParserFactory;
import org.slizaa.scanner.jtype.model.IFieldNode;
import org.slizaa.scanner.jtype.model.IMethodNode;
import org.slizaa.scanner.jtype.model.ITypeNode;
import org.slizaa.scanner.jtype.model.IVisibility;
import org.slizaa.scanner.jtype.model.JTypeLabel;
import org.slizaa.scanner.jtype.model.JTypeModelRelationshipType;
import org.slizaa.scanner.jtype.model.JavaTypeUtils;

/**
 */
public class JTypeClassVisitor extends ClassVisitor {

  /** - */
  private IModifiableNode            _typeBean;

  /** - */
  private TypeLocalReferenceCache    _classLocalTypeReferenceCache;

  /** - */
  private JTypeByteCodeParserFactory _parserFactory;

  /**
   * <p>
   * Creates a new instance of type {@link JTypeClassVisitor}.
   * </p>
   * 
   * @param batchInserter
   */
  public JTypeClassVisitor(JTypeByteCodeParserFactory parserFactory) {
    super(Opcodes.ASM6);

    //
    _parserFactory = checkNotNull(parserFactory);

    //
    _classLocalTypeReferenceCache = new TypeLocalReferenceCache(_parserFactory.getDatatypeNodeProviderMap());
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public TypeLocalReferenceCache getTypeLocalReferenceCache() {
    return _classLocalTypeReferenceCache;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit(final int version, final int access, final String name, final String signature,
      final String superName, final String[] interfaces) {

    //
    _typeBean = NodeFactory.createNode();

    // add type bean to type local cache
    _classLocalTypeReferenceCache.setTypeBean(_typeBean);

    // add the type of the type
    _typeBean.addLabel(JTypeLabel.TYPE);
    _typeBean.addLabel(getJTypeLabel(access));

    // class name
    _typeBean.putProperty(ITypeNode.FQN, name.replace('/', '.'));
    _typeBean.putProperty(ITypeNode.NAME, JavaTypeUtils.getSimpleName(name.replace('/', '.')));

    // class version
    _typeBean.putProperty(ITypeNode.CLASS_VERSION, Integer.toString(version));

    // deprecated
    if ((access & Opcodes.ACC_DEPRECATED) == Opcodes.ACC_DEPRECATED) {
      _typeBean.putProperty(ITypeNode.DEPRECATED, true);
    }

    // // access flags
    // _typeBean.putProperty(ITypeNode.ACCESS_FLAGS, Integer.toHexString(access).toUpperCase());

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
      _typeBean.putProperty(ITypeNode.VISIBILITY, IVisibility.PUBLIC);
    }
    //
    else if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
      _typeBean.putProperty(ITypeNode.VISIBILITY, IVisibility.PRIVATE);
    }
    //
    else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
      _typeBean.putProperty(ITypeNode.VISIBILITY, IVisibility.PROTECTED);
    }
    //
    else {
      _typeBean.putProperty(ITypeNode.VISIBILITY, IVisibility.DEFAULT);
    }

    // TODO!!
    if (signature != null) {

      // set signature
      _typeBean.putProperty(ITypeNode.SIGNATURE, signature);

      JTypeTypeSignatureVisitor sv = new JTypeTypeSignatureVisitor(_typeBean, _classLocalTypeReferenceCache);
      new SignatureReader(signature).accept(sv);
    }

    // add 'extends' references
    _classLocalTypeReferenceCache.addTypeReference(_typeBean, superName, JTypeModelRelationshipType.EXTENDS);

    // add 'implements' references
    for (String ifaceName : interfaces) {

      switch (getJTypeLabel(access)) {
      case CLASS:
        _classLocalTypeReferenceCache.addTypeReference(_typeBean, ifaceName, JTypeModelRelationshipType.IMPLEMENTS);
        break;
      case INTERFACE:
        _classLocalTypeReferenceCache.addTypeReference(_typeBean, ifaceName, JTypeModelRelationshipType.EXTENDS);
        break;
      case ANNOTATION:
        _classLocalTypeReferenceCache.addTypeReference(_typeBean, ifaceName, JTypeModelRelationshipType.EXTENDS);
        break;
      default:
        break;
      }
    }
  }

  /**
   * @inheritDoc
   */
  @Override
  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

    // create and add new method bean
    IModifiableNode annotationInstanceBean = NodeFactory.createNode();
    _typeBean.addRelationship(annotationInstanceBean, JTypeModelRelationshipType.ANNOTATED_BY);

    // set labels
    annotationInstanceBean.addLabel(JTypeLabel.ANNOTATION_INSTANCE);

    // class annotation
    _classLocalTypeReferenceCache.addTypeReference(annotationInstanceBean, Type.getType(desc),
        JTypeModelRelationshipType.IS_OF_TYPE);

    //
    return new JTypeAnnotationVisitor();
  }

  /**
   * @inheritDoc
   */
  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

    // create and add new method bean
    IModifiableNode methodBean = NodeFactory.createNode();
    _typeBean.addRelationship(methodBean, CoreModelRelationshipType.CONTAINS);

    // add method name
    String methodSignature = Utils.getMethodSignature(name, desc);
    methodBean.putProperty(IMethodNode.NAME, name);
    methodBean.putProperty(IMethodNode.FQN, methodSignature);

    // set labels
    methodBean.addLabel(JTypeLabel.METHOD);
    if (methodSignature.startsWith("void <init>")) {
      methodBean.addLabel(JTypeLabel.CONSTRUCTOR);
    }

    // signature
    if (signature != null) {
      methodBean.putProperty("signature", signature);
      new SignatureReader(signature).accept(new JTypeMethodSignatureVisitor(methodBean, _classLocalTypeReferenceCache));
    }

    //
    if ((access & Opcodes.ACC_NATIVE) == Opcodes.ACC_NATIVE) {
      methodBean.putProperty(IMethodNode.NATIVE, true);
    }

    if ((access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT) {
      methodBean.putProperty(IMethodNode.ABSTRACT, true);
    }
    if ((access & Opcodes.ACC_SYNTHETIC) == Opcodes.ACC_SYNTHETIC) {
      methodBean.putProperty(IMethodNode.SYNTHETIC, true);
    }
    if ((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC) {
      methodBean.putProperty(IMethodNode.STATIC, true);
    }
    if ((access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL) {
      methodBean.putProperty(IMethodNode.FINAL, true);
    }

    // Access modifiers: public, protected, and private
    if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
      methodBean.putProperty(IMethodNode.VISIBILITY, IVisibility.PUBLIC);
    } else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
      methodBean.putProperty(IMethodNode.VISIBILITY, IVisibility.PROTECTED);
    } else if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
      methodBean.putProperty(IMethodNode.VISIBILITY, IVisibility.PRIVATE);
    } else {
      methodBean.putProperty(IMethodNode.VISIBILITY, IVisibility.DEFAULT);
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
    return new JTypeMethodVisitor(_typeBean, methodBean, _classLocalTypeReferenceCache);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FieldVisitor visitField(int access, String name, String desc, String signature, final Object value) {

    // create bean and add it to the type bean
    IModifiableNode fieldBean = NodeFactory.createNode();
    fieldBean.addLabel(JTypeLabel.FIELD);
    _typeBean.addRelationship(fieldBean, CoreModelRelationshipType.CONTAINS);

    // TODO!!
    fieldBean.putProperty(IMethodNode.FQN, Utils.getFieldSignature(name, desc));

    // get the type
    addReference(fieldBean, Type.getType(desc), JTypeModelRelationshipType.IS_OF_TYPE);

    //
    fieldBean.putProperty(IFieldNode.NAME, name);

    // access flags
    fieldBean.putProperty(IFieldNode.ACCESS_FLAGS, Integer.toHexString(access).toUpperCase());

    // Access modifiers: public, protected, and private
    if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
      fieldBean.putProperty(IFieldNode.VISIBILITY, IVisibility.PUBLIC);
    } else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
      fieldBean.putProperty(IFieldNode.VISIBILITY, IVisibility.PROTECTED);
    } else if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
      fieldBean.putProperty(IFieldNode.VISIBILITY, IVisibility.PRIVATE);
    } else {
      fieldBean.putProperty(IFieldNode.VISIBILITY, IVisibility.DEFAULT);
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
      // JTypeTypeSignatureVisitor sv = new JTypeTypeSignatureVisitor(0);
      // SignatureReader r = new SignatureReader(signature);
      // r.acceptType(sv);
    }

    // TODO
    return new JTypeFieldVisitor(this);
  }

  @Override
  public void visitSource(String source, String debug) {
    _typeBean.putProperty(ITypeNode.SOURCE_FILE_NAME, source);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visitOuterClass(String owner, String name, String rawSignature) {
    // System.out.println("visitOuterClass: " + owner + " : " + name + " : " + rawSignature);

    // owner
    _classLocalTypeReferenceCache.addTypeReference(_typeBean, Type.getObjectType(owner),
        JTypeModelRelationshipType.REFERENCES);

    // TODO: EnclosingMethod!
    if (name != null && rawSignature != null) {

      // return type
      Type returnType = org.objectweb.asm.Type.getReturnType(rawSignature);
      if (!Utils.isVoidOrPrimitive(returnType)) {
        _classLocalTypeReferenceCache.addTypeReference(_typeBean, Utils.resolveArrayType(returnType).getClassName(),
            JTypeModelRelationshipType.REFERENCES);
      }

      // arg types type
      org.objectweb.asm.Type[] types = org.objectweb.asm.Type.getArgumentTypes(rawSignature);
      for (int i = 0; i < types.length; i++) {
        if (!Utils.isVoidOrPrimitive(types[i])) {

          // TODO: array types!
          _classLocalTypeReferenceCache.addTypeReference(_typeBean, Utils.resolveArrayType(types[i]).getClassName(),
              JTypeModelRelationshipType.REFERENCES);
        }
      }
    }
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

    // System.out.println(_typeBean.getFullyQualifiedName() + " - visitInnerClass(" + name + ", " + outerName + ", "
    // + innerName + ", " + access + ")");

    //
    if (name.replace('/', '.').equals(_typeBean.getFullyQualifiedName())) {

      if (outerName != null) {
        _classLocalTypeReferenceCache.addTypeReference(_typeBean, outerName.replace('/', '.'),
            JTypeModelRelationshipType.IS_INNER_CLASS_DEFINED_BY);
      }

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
          _typeBean.putProperty(ITypeNode.INNER_CLASS_ACCESS_LEVEL, IVisibility.PUBLIC);
        }
        //
        else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
          _typeBean.putProperty(ITypeNode.INNER_CLASS_ACCESS_LEVEL, IVisibility.PROTECTED);
        }
        //
        else if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
          _typeBean.putProperty(ITypeNode.INNER_CLASS_ACCESS_LEVEL, IVisibility.PRIVATE);
        }
        //
        else {
          _typeBean.putProperty(ITypeNode.INNER_CLASS_ACCESS_LEVEL, IVisibility.DEFAULT);
        }
      }
    }

    // http://stackoverflow.com/questions/24622658/access-flag-for-private-inner-classes-in-java-spec-inconsistent-with-reflectio
    // TODO
    else if (outerName != null) {

      //
      if (outerName.replace('/', '.').equals(_typeBean.getFullyQualifiedName())) {
        _classLocalTypeReferenceCache.addTypeReference(_typeBean, name.replace('/', '.'),
            JTypeModelRelationshipType.DEFINES_INNER_CLASS);
      }
      //
      else {
        _classLocalTypeReferenceCache.addTypeReference(_typeBean, outerName.replace('/', '.'),
            JTypeModelRelationshipType.REFERENCES);
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
      return fieldBean.addRelationship(Utils.getPrimitiveDatatypeNode(t, _parserFactory.getDatatypeNodeProviderMap()),
          relationshipType);
    } else {
      return _classLocalTypeReferenceCache.addTypeReference(fieldBean, t.getClassName(), relationshipType);
    }
  }

  public JTypeLabel getJTypeLabel(int access) {

    // handle annotation
    if ((access & Opcodes.ACC_ANNOTATION) == Opcodes.ACC_ANNOTATION) {
      return JTypeLabel.ANNOTATION;
    }
    // handle interface
    else if ((access & Opcodes.ACC_INTERFACE) == Opcodes.ACC_INTERFACE) {
      return JTypeLabel.INTERFACE;
    }
    // handle enum
    else if ((access & Opcodes.ACC_ENUM) == Opcodes.ACC_ENUM) {
      return JTypeLabel.ENUM;
    }
    // handle class
    else {
      return JTypeLabel.CLASS;
    }
  }
}
