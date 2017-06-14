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
package org.slizaa.scanner.importer.content;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;

import org.eclipse.core.runtime.CoreException;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * Encapsulates a path that can contain variables, e.g <code>${project_loc:myEclipseProject/lib/myJar.jar}</code>. This
 * class provides several convenience methods to resolve the variable path and retrieve the corresponding {@link File}.
 * </p>
 * <p>
 * Instances of this class are automatically externalized as JSON strings when used in a IProjectContentProvider
 * implementation (if the corresponding fields are annotated with an {@link Expose} annotation).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noextend This class is not intended to be extended by clients.
 */
public class VariablePath {

  /** the path that may contains variables */
  @Expose
  @SerializedName("path")
  private final String _path;

  /**
   * <p>
   * Creates a new instance of type {@link VariablePath}. Must not be {@code null} and not empty.
   * </p>
   * 
   * @param path
   *          the path that may contains variables
   */
  public VariablePath(String path) {
    checkNotNull(path);
    checkState(!path.isEmpty());

    // set the path
    _path = path;
  }

  /**
   * <p>
   * Returns the raw, unresolved path that may contains variables.
   * </p>
   * 
   * @return the raw, unresolved path that may contains variables.
   */
  public String getUnresolvedPath() {
    return _path;
  }

  /**
   * <p>
   * Returns the resolved path using the eclipse {@link IStringVariableManager}. If the path contains one or more
   * undefined variables, a {@link CoreException} will be thrown.
   * </p>
   * 
   * @return the resolved path
   * @throws CoreException
   *           if the path contains one or more undefined variables
   */
  public String getResolvedPath() {

    // // get the IVariablePathResolver
    // IVariablePathResolver variablePathResolver = Activator.getVariablePathResolver();
    //
    // // return the resolved path
    // return variablePathResolver != null ? variablePathResolver.resolve(this) :
    return _path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "VariablePath [_path=" + _path + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = 31;
    result = result + getResolvedPath().hashCode();
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

    VariablePath other = (VariablePath) obj;
    try {
      return getResolvedPath().equals(other.getResolvedPath());
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}
