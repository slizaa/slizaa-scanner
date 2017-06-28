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

import static com.google.common.base.Preconditions.checkNotNull;

import org.objectweb.asm.Type;
import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.api.model.IRelationship;
import org.slizaa.scanner.api.model.RelationshipType;
import org.slizaa.scanner.jtype.model.internal.primitvedatatypes.IPrimitiveDatatypeNodeProvider;

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
  public IRelationship addTypeReference(final IModifiableNode startNode, final String referencedTypeName,
      final RelationshipType relationshipType) {

    //
    if (referencedTypeName == null) {
      return null;
    }

    //
    return startNode.addRelationship(_typeReferenceNodeCache.getUnchecked(referencedTypeName), relationshipType);
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

    String referencedTypeName = Utils.getFullyQualifiedTypeName(referencedType);

    //
    if (referencedTypeName == null) {
      throw new RuntimeException(referencedType.toString());
      // return null;
    }

    //
    return addTypeReference(startNode, referencedTypeName, relationshipType);
  }

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
}
