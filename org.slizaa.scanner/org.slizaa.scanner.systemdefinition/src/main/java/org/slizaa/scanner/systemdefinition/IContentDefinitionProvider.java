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
package org.slizaa.scanner.systemdefinition;

import java.util.List;

import org.slizaa.scanner.importer.spi.content.IContentDefinition;

/**
 * <p>
 * </p>
 * 
 * <p>
 * Note: Implementations of this class must be subclasses of AbstractProjectContentProvider.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IContentDefinitionProvider {

  /**
   * <p>
   * Returns the internal identifier of this content entry provider.
   * </p>
   * 
   * @return the internal identifier of this content entry provider.
   */
  String getId();

  /**
   * <p>
   * Returns a (unmodifiable) list of {@link IContentDefinition IContentDefinitions}.
   * </p>
   * 
   * @return a (unmodifiable) list of {@link IContentDefinition IContentDefinitions}.
   */
  List<IContentDefinition> getContentDefinitions();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  IContentDefinitionProvider copyInstance();
  
  /**
   * <p>
   * </p>
   *
   * @param provider
   */
  void merge(IContentDefinitionProvider provider);
}
