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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Type;
import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.api.model.IRelationship;
import org.slizaa.scanner.api.model.RelationshipType;
import org.slizaa.scanner.jtype.bytecode.IPrimitiveDatatypeNodeProvider;
import org.slizaa.scanner.jtype.model.JTypeModelRelationshipType;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * <p>
 * A cache that stores type references.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class TypeLocalReferenceCache {

  /** - */
  private IPrimitiveDatatypeNodeProvider                 _primitiveDatatypeNodes;

  /** - */
  private LoadingCache<String, INode>                    _typeReferenceNodeCache;

  /** - */
  private LoadingCache<FieldDescriptor, IModifiableNode> _fieldReferenceNodeCache;

  /** - */
  private List<INode>                                    _dependsOnRelationshipTargets;

  /** - */
  private IModifiableNode                                _typeBean;

  /**
   * <p>
   * Creates a new instance of type {@link TypeLocalReferenceCache}.
   * </p>
   */
  public TypeLocalReferenceCache(IPrimitiveDatatypeNodeProvider primitiveDatatypeNodes) {
    _primitiveDatatypeNodes = checkNotNull(primitiveDatatypeNodes);

    //
    _typeReferenceNodeCache = CacheBuilder.newBuilder().build(new CacheLoader<String, INode>() {
      public INode load(String referencedTypeName) {
        return JTypeNodeHelper.createTypeReferenceNode(referencedTypeName);
      }
    });

    //
    _fieldReferenceNodeCache = CacheBuilder.newBuilder().build(new CacheLoader<FieldDescriptor, IModifiableNode>() {
      public IModifiableNode load(FieldDescriptor referencedField) {
        return JTypeNodeHelper.createFieldReferenceNode(referencedField);
      }
    });

    //
    _dependsOnRelationshipTargets = new ArrayList<>();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Set<String> getAllReferencedTypes() {
    return _typeReferenceNodeCache.asMap().keySet();
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
   * <p>
   * </p>
   *
   * @param typeBean
   */
  public void setTypeBean(IModifiableNode typeBean) {
    _typeBean = typeBean;
  }

  /**
   * <p>
   * </p>
   * 
   * @param startNode
   * @param fieldDescriptor
   * @param relationshipType
   * @return
   */
  public IRelationship addFieldReference(final IModifiableNode startNode, final FieldDescriptor fieldDescriptor,
      final RelationshipType relationshipType) {

    //
    INode fieldTypeBean = _typeReferenceNodeCache.getUnchecked(fieldDescriptor.getFieldType().replace('/', '.'));

    //
    if (!fieldDescriptor.isPrimitive()) {
      addDependsOnRelationship(fieldTypeBean);
    }

    //
    INode fieldOwnerBean = _typeReferenceNodeCache.getUnchecked(fieldDescriptor.getOwnerTypeName().replace('/', '.'));
    addDependsOnRelationship(fieldOwnerBean);

    // field access
    return startNode.addRelationship(_fieldReferenceNodeCache.getUnchecked(fieldDescriptor), relationshipType);
  }

  /**
   * <p>
   * </p>
   * 
   * @param referencedTypeName
   * @param relationshipType
   */
  public IRelationship addTypeReference(final IModifiableNode startNode, String referencedTypeName,
      final RelationshipType relationshipType) {

    //
    if (referencedTypeName == null) {
      return null;
    }

    // TODO
    if (referencedTypeName.equals("boolean")) {
      throw new RuntimeException();
    }
    // TODO
    if (referencedTypeName.equals("int")) {
      throw new RuntimeException();
    }

    // TODO
    if (referencedTypeName.endsWith("[]")) {
      throw new RuntimeException();
    }

    referencedTypeName = referencedTypeName.replace('/', '.');

    //
    INode targetBean = _typeReferenceNodeCache.getUnchecked(referencedTypeName);
    addDependsOnRelationship(targetBean);
    return startNode.addRelationship(targetBean, relationshipType);
  }

  public IRelationship addInnerClass(final IModifiableNode outerClass, IModifiableNode innerClass,
      final RelationshipType relationshipType) {

    checkNotNull(outerClass);
    checkNotNull(innerClass);
    checkNotNull(relationshipType);

    //
    addDependsOnRelationship(innerClass);
    return outerClass.addRelationship(innerClass, relationshipType);
  }

  /**
   * <p>
   * </p>
   * 
   * @param referencedType
   * @param relationshipType
   */
  public IRelationship addTypeReference(final IModifiableNode startNode, final Type referencedType,
      final RelationshipType relationshipType) {

    //
    if (referencedType == null) {
      throw new RuntimeException();
      // return null;
    }
    
    //
    if (Utils.isVoidOrPrimitive(referencedType)) {
      throw new RuntimeException();
      // return null;
    }

    String referencedTypeName = Utils.getFullyQualifiedTypeName(referencedType);

    //
    if (referencedTypeName == null) {
      throw new RuntimeException(referencedType.toString());
      // return null;
    }

    //
    return addTypeReference(startNode, referencedTypeName, relationshipType);
  }

  /**
   * <p>
   * </p>
   *
   * @param startNode
   * @param referencedTypes
   * @param relationshipType
   */
  public void addTypeReference(final IModifiableNode startNode, final Type[] referencedTypes,
      final RelationshipType relationshipType) {

    //
    if (referencedTypes == null) {
      return;
    }

    //
    for (Type referencedType : referencedTypes) {
      addTypeReference(startNode, referencedType, relationshipType);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param targetBean
   */
  private void addDependsOnRelationship(INode targetBean) {
    if (!targetBean.getFullyQualifiedName().equals(_typeBean.getFullyQualifiedName())
        && !_dependsOnRelationshipTargets.contains(targetBean)) {

      _dependsOnRelationshipTargets.add(targetBean);
      _typeBean.addRelationship(targetBean, JTypeModelRelationshipType.DEPENDS_ON);
    }
  }
}
