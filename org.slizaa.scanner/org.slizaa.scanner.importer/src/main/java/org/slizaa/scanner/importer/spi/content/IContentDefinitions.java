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

import java.util.List;

import org.eclipse.core.runtime.AssertionFailedException;

/**
 * <p>
 * Represents the definition of the system that should be analyzed. A {@link IContentDefinitions} can be created using the
 * {@link ISystemDefinitionFactory}.
 * </p>
 * <p>
 * 
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IContentDefinitions {

  /**
   * <p>
   * Returns a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
   * </p>
   * <p>
   * This method throws an {@link AssertionFailedException} if the {@link IContentDefinitions} is not initialized.
   * </p>
   * 
   * @return a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
   */
  List<IContentDefinition> getContentDefinitions();
}