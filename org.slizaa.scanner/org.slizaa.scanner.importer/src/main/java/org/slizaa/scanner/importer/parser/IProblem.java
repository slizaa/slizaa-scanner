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
package org.slizaa.scanner.importer.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.scanner.importer.content.IResource;
import org.slizaa.scanner.importer.content.ISystemDefinition;

/**
 * <p>
 * Common interface for problems and errors that occur while parsing the content of a
 * {@link ISystemDefinition}.
 * </p>
 * <p>
 * Clients may implement this interface.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IProblem {

  /**
   * <p>
   * Returns {@link IProjectContentResource} of the associated resource.
   * </p>
   * 
   * @return the {@link IProjectContentResource} of the associated resource.
   */
  IResource getResource();

  /**
   * <p>
   * Returns <code>true</code> if this problem is an error, <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if this problem is an error, <code>false</code> otherwise.
   */
  boolean isError();

  /**
   * <p>
   * Returns the line number in source where the problem begins or -1 if unknown
   * </p>
   * 
   * @return the line number in source where the problem begins
   */
  int getSourceLineNumber();

  /**
   * <p>
   * Returns the message for this problem.
   * </p>
   * 
   * @return the message for this problem.
   */
  String getMessage();

  /**
   * <p>
   * Returns the start position of the problem (inclusive), or -1 if unknown.
   * </p>
   * 
   * @return the start position of the problem (inclusive), or -1 if unknown.
   */
  int getSourceStart();

  /**
   * <p>
   * Answer the end position of the problem (inclusive), or -1 if unknown.
   * </p>
   * 
   * @return the end position of the problem (inclusive), or -1 if unknown
   */
  int getSourceEnd();

  /**
   * <p>
   * </p>
   * 
   * @author Nils Hartmann (nils@nilshartmann.net)
   */
  public static class DefaultProblem implements IProblem {

    /** - */
    private final IResource _resource;

    /** - */
    private final String    _message;

    /**
     * @param resource
     * @param message
     */
    public DefaultProblem(IResource resource, String message) {

      checkNotNull(resource);
      checkNotNull(message);

      _resource = resource;
      _message = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IResource getResource() {
      return _resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
      return this._message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isError() {
      return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceLineNumber() {
      return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceStart() {
      return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceEnd() {
      return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      return "DefaultProblem [_resource=" + _resource + ", _message=" + _message + "]";
    }
  }
}
