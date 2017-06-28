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
package org.slizaa.scanner.model.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slizaa.scanner.model.IModifiableNode;
import org.slizaa.scanner.model.INode;
import org.slizaa.scanner.model.IRelationship;
import org.slizaa.scanner.model.Label;
import org.slizaa.scanner.model.RelationshipType;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class NodeBean implements IModifiableNode {

  //
  private static final Map<RelationshipType, List<IRelationship>> EMPTY_MAP = Collections.emptyMap();

  /** - */
  private long                                                    _nodeId   = -1;

  /** the properties */
  private Map<String, Object>                                     _properties;

  /** the labels */
  private List<Label>                                             _labels;

  /** the contained children */
  private LoadingCache<RelationshipType, List<IRelationship>>     _relationships;

  /**
   * <p>
   * Creates a new instance of type {@link NodeBean}.
   * </p>
   * 
   * @param nodeId
   */
  public NodeBean(long nodeId) {
    this();

    //
    _nodeId = nodeId;
  }

  /**
   * <p>
   * Creates a new instance of type {@link NodeBean}.
   * </p>
   * 
   * @param _batchInserter
   */
  public NodeBean() {

    // we always use labels and properties, so there is no need to lazy create these fields
    _labels = new ArrayList<Label>(0);
    _properties = new HashMap<String, Object>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getId() {
    return _nodeId;
  }

  @Override
  public String getNodetype() {
    return (String) _properties.get(INode.NODETYPE);
  }

  @Override
  public String getFullyQualifiedName() {
    return (String) _properties.get(INode.FQN);
  }

  @Override
  public String getName() {
    return (String) _properties.get(INode.NAME);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean hasNodeId() {
    return _nodeId != -1;
  }

  /**
   * <p>
   * </p>
   * 
   * @param nodeId
   */
  public void setNodeId(long nodeId) {
    checkState(_nodeId == -1, "Node id already has been set.");
    _nodeId = nodeId;
  }

  /**
   * <p>
   * Adds the specified label.
   * </p>
   * 
   * @param label
   *          the label to add. Must not be <code>null</code>.
   */
  public void addLabel(Label label) {
    checkNotNull(label);

    _labels.add(label);
  }

  /**
   * <p>
   * </p>
   * 
   * @param label
   * @return
   */
  public boolean containsLabel(Label label) {
    checkNotNull(label);

    return _labels.contains(label);
  }

  /**
   * <p>
   * </p>
   * 
   * @param key
   * @param value
   */
  public void putProperty(String key, Object value) {
    checkNotNull(key);
    checkNotNull(value);

    _properties.put(key, value);
  }

  /**
   * <p>
   * </p>
   * 
   * @param key
   * @return
   */
  public Object getProperty(String key) {
    checkNotNull(key);

    return _properties.get(key);
  }

  /**
   * {@inheritDoc}
   */
  public IRelationship addRelationship(INode targetBean, RelationshipType relationshipType) {
    checkNotNull(targetBean);
    checkNotNull(relationshipType);

    Relationship result = new Relationship(targetBean, relationshipType);
    synchronized (relationships()) {
      relationships().getUnchecked(relationshipType).add(result);
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public void clearRelationships() {
    synchronized (relationships()) {
      relationships().invalidateAll();
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Map<String, Object> getProperties() {
    return Collections.unmodifiableMap(_properties);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public List<Label> getLabels() {
    return Collections.unmodifiableList(_labels);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Map<RelationshipType, List<IRelationship>> getRelationships() {
    return _relationships == null ? EMPTY_MAP : _relationships.asMap();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + " [_labels=" + _labels + ", _properties " + _properties + "]";
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private LoadingCache<RelationshipType, List<IRelationship>> relationships() {

    //
    if (_relationships == null) {

      //
      _relationships = CacheBuilder.newBuilder().build(new CacheLoader<RelationshipType, List<IRelationship>>() {
        public List<IRelationship> load(RelationshipType key) {
          return new LinkedList<>();
        }
      });
    }
    //
    return _relationships;
  }

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private static class Relationship implements IRelationship {

    //
    private static final Map<String, Object> EMPTY_MAP = Collections.emptyMap();

    /** - */
    private INode                            _targetBean;

    /** - */
    private RelationshipType                 _relationshipType;

    /** - */
    private Map<String, Object>              _properties;

    /**
     * <p>
     * Creates a new instance of type {@link Relationship}.
     * </p>
     * 
     * @param targetBean
     * @param relationshipType
     */
    public Relationship(INode targetBean, RelationshipType relationshipType) {
      _targetBean = checkNotNull(targetBean);
      _relationshipType = checkNotNull(relationshipType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public INode getTargetBean() {
      return _targetBean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationshipType getRelationshipType() {
      return _relationshipType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getRelationshipProperties() {
      return _properties == null ? EMPTY_MAP : Collections.unmodifiableMap(_properties);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void putRelationshipProperty(String key, Object value) {
      checkNotNull(key);
      checkNotNull(value);
      
      if (_properties == null) {
        _properties = new HashMap<>();
      }
      
       _properties.put(key, value);
    }
  }
}