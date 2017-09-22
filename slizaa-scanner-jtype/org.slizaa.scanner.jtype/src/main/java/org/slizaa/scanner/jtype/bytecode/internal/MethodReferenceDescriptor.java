package org.slizaa.scanner.jtype.bytecode.internal;

import static com.google.common.base.Preconditions.checkNotNull;

public class MethodReferenceDescriptor {

  /** - */
  private String  _ownerTypeName;

  /** - */
  private String  _methodName;

  /** - */
  private String  _methodSignature;

  /** - */
  private boolean _isInterface;

  /**
   * @param ownerTypeName
   * @param methodName
   * @param methodSignature
   * @param isStatic
   */
  public MethodReferenceDescriptor(String ownerTypeName, String methodName, String methodSignature, boolean isInterface) {
    checkNotNull(ownerTypeName);
    checkNotNull(methodName);
    checkNotNull(methodSignature);
    checkNotNull(isInterface);

    _ownerTypeName = ownerTypeName;
    _methodName = methodName;
    _methodSignature = methodSignature;
    _isInterface = isInterface;
  }

  public String getOwnerTypeName() {
    return _ownerTypeName;
  }

  public String getMethodName() {
    return _methodName;
  }

  public String getMethodSignature() {
    return _methodSignature;
  }

  public boolean isStatic() {
    return _isInterface;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (_isInterface ? 1231 : 1237);
    result = prime * result + ((_methodName == null) ? 0 : _methodName.hashCode());
    result = prime * result + ((_methodSignature == null) ? 0 : _methodSignature.hashCode());
    result = prime * result + ((_ownerTypeName == null) ? 0 : _ownerTypeName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MethodReferenceDescriptor other = (MethodReferenceDescriptor) obj;
    if (_isInterface != other._isInterface)
      return false;
    if (_methodName == null) {
      if (other._methodName != null)
        return false;
    } else if (!_methodName.equals(other._methodName))
      return false;
    if (_methodSignature == null) {
      if (other._methodSignature != null)
        return false;
    } else if (!_methodSignature.equals(other._methodSignature))
      return false;
    if (_ownerTypeName == null) {
      if (other._ownerTypeName != null)
        return false;
    } else if (!_ownerTypeName.equals(other._ownerTypeName))
      return false;
    return true;
  }
}
