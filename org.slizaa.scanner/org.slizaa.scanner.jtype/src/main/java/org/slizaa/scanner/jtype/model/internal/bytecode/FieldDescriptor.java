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

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FieldDescriptor {

  /** - */
  private String  _ownerTypeName;

  /** - */
  private String  _fieldName;

  /** - */
  private String  _fieldType;

  /** - */
  private boolean _isStatic;

  /**
   * <p>
   * Creates a new instance of type {@link FieldDescriptor}.
   * </p>
   * 
   * @param ownerTypeName
   * @param fieldName
   * @param fieldType
   * @param isStatic
   */
  public FieldDescriptor(String ownerTypeName, String fieldName, String fieldType, boolean isStatic) {

    checkNotNull(ownerTypeName);
    checkNotNull(fieldName);
    checkNotNull(fieldType);

    //
    _ownerTypeName = ownerTypeName;
    _fieldName = fieldName;
    _fieldType = fieldType;
    _isStatic = isStatic;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getOwnerTypeName() {
    return _ownerTypeName;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getFieldName() {
    return _fieldName;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getFieldType() {
    return _fieldType;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean isStatic() {
    return _isStatic;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_fieldName == null) ? 0 : _fieldName.hashCode());
    result = prime * result + ((_fieldType == null) ? 0 : _fieldType.hashCode());
    result = prime * result + (_isStatic ? 1231 : 1237);
    result = prime * result + ((_ownerTypeName == null) ? 0 : _ownerTypeName.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FieldDescriptor other = (FieldDescriptor) obj;
    if (_fieldName == null) {
      if (other._fieldName != null)
        return false;
    } else if (!_fieldName.equals(other._fieldName))
      return false;
    if (_fieldType == null) {
      if (other._fieldType != null)
        return false;
    } else if (!_fieldType.equals(other._fieldType))
      return false;
    if (_isStatic != other._isStatic)
      return false;
    if (_ownerTypeName == null) {
      if (other._ownerTypeName != null)
        return false;
    } else if (!_ownerTypeName.equals(other._ownerTypeName))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FieldDescriptor [_ownerTypeName=" + _ownerTypeName + ", _fieldName=" + _fieldName + ", _fieldType="
        + _fieldType + ", _isStatic=" + _isStatic + "]";
  }
}
