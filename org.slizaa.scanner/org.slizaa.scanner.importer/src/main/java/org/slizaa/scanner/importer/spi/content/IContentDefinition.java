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
package org.slizaa.scanner.importer.spi.content;

import java.util.Collection;
import java.util.Set;

import org.slizaa.scanner.model.resource.ResourceType;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IContentDefinition {

  /**
   * <p>
   * Returns the internal identifier of this content entry.
   * </p>
   * 
   * @return the internal identifier of this content entry.
   */
  String getId();

  /**
   * <p>
   * Returns the name of this content entry.
   * </p>
   * 
   * @return the name of this content entry.
   */
  String getName();

  /**
   * <p>
   * Returns the version of this content entry.
   * </p>
   * 
   * @return the version of this content entry.
   */
  String getVersion();

  /**
   * <p>
   * Return <code>true</code> if this content entry is a resource entry that should be parsed and analyzed,
   * <code>false</code> otherwise.
   * </p>
   * 
   * @return <code>true</code> if this content entry is a resource entry that should be parsed and analyzed,
   *         <code>false</code> otherwise.
   */
  boolean isAnalyze();

  /**
   * <p>
   * Returns the {@link AnalyzeMode} for this {@link IContentDefinition}.
   * </p>
   * 
   * @return the {@link AnalyzeMode} for this {@link IContentDefinition}
   */
  AnalyzeMode getAnalyzeMode();

  /**
   * <p>
   * Returns a {@link Set} of all resources of the specified type
   * </p>
   * <p>
   * If this content entry is not a resource content ( <code>isAnalyze()</code> returns <code>false</code>), an empty
   * set will be returned.
   * </p>
   * 
   * @param type
   * @return a Set of resources, never null.
   */
  Collection<IResource> getResources(ResourceType type);

  /**
   * <p>
   * Returns a {@link Set} of all binary resources. This is a convenience method for {@link #getResources(ResourceType)
   * getResources(ContentType.BINARY)}
   * </p>
   * <p>
   * If this content entry is not a resource content ( <code>isAnalyze()</code> returns <code>false</code>), an empty
   * set will be returned.
   * </p>
   * 
   * @return a Set of resources, never null.
   */
  Collection<IResource> getBinaryResources();

  /**
   * <p>
   * Returns all source resources. This is a convenience method for {@link #getResources(ResourceType)
   * getResources(ContentType.SOURCE)}
   * </p>
   * <p>
   * If this content entry is not a resource content ( <code>isAnalyze()</code> returns <code>false</code>), an empty
   * set will be returned.
   * </p>
   * 
   * @return a Set of resources, never null.
   */
  Collection<IResource> getSourceResources();
}
