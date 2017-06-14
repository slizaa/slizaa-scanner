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

/**
 * <p>
 * A {@link ResourceChangedEvent} is thrown if the content (not the description!) of a {@link ISystemDefinition} has
 * changed.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noextend This class is not intended to be subclasses by clients.
 */
public class ResourceChangedEvent {

  /**
   * <p>
   * Defined the possible types of a {@link ResourceChangedEvent}.
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   * 
   * @noextend This class is not intended to be subclasses by clients.
   */
  public enum Type {
    ADDED, REMOVED, MODIFIED;
  }

  /** the system definition */
  private ISystemDefinition  _systemDefinition;

  /** the content definition */
  private IContentDefinition _contentDefinition;

  /** the resource that has changed */
  private IResource          _resource;

  /** type */
  private Type               _type;

  /**
   * <p>
   * Creates a new instance of type {@link ResourceChangedEvent}.
   * </p>
   * 
   * @param systemDefinition
   * @param resource
   */
  public ResourceChangedEvent(ISystemDefinition systemDefinition, IContentDefinition contentDefinition,
      IResource resource, Type type) {
    checkNotNull(systemDefinition, "Parameter 'systemDefinition' must not be null.");
    checkNotNull(contentDefinition, "Parameter 'contentDefinition' must not be null.");
    checkNotNull(resource, "Parameter 'resource' must not be null.");
    checkNotNull(type, "Parameter 'type' must not be null.");

    _systemDefinition = systemDefinition;
    _contentDefinition = contentDefinition;
    _resource = resource;
    _type = type;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Type getType() {
    return _type;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public IResource getResource() {
    return _resource;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public IContentDefinition getContentDefinition() {
    return _contentDefinition;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public ISystemDefinition getSystemDefinition() {
    return _systemDefinition;
  }
}
