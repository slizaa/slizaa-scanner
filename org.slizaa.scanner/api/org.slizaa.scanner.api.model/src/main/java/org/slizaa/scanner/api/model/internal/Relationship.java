package org.slizaa.scanner.api.model.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.api.model.IRelationship;
import org.slizaa.scanner.api.model.RelationshipType;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Relationship implements IRelationship {

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

  @Override
  public String toString() {
    return "Relationship [_targetBean=" + _targetBean.getFullyQualifiedName() + "/" + _targetBean.getName()
        + ", _relationshipType=" + _relationshipType + ", _properties=" + _properties + "]";
  }
}