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

/**
 * <p>
 * Specifies how an {@link IContentDefinition} entry in a {@link ISystemDefinition} should be analyzed.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 * @noextend This class is not intended to be extended by clients.
 */
public enum AnalyzeMode {

  /** Analyze only binaries of a content entry */
  BINARIES_ONLY,

  /** Analyze sources and binaries of a content entry */
  BINARIES_AND_SOURCES,

  /** Do not analyze this content at all */
  DO_NOT_ANALYZE;

  /**
   * <p>
   * Returns {@code true} if this instance either is {@link #BINARIES_ONLY} or {@link #BINARIES_AND_SOURCES},
   * {@code false} otherwise.
   * </p>
   * 
   * @return {@code true} if this instance either is {@link #BINARIES_ONLY} or {@link #BINARIES_AND_SOURCES},
   *         {@code false} otherwise.
   */
  public boolean isAnalyze() {
    return this == BINARIES_ONLY || this == BINARIES_AND_SOURCES;
  }
}
