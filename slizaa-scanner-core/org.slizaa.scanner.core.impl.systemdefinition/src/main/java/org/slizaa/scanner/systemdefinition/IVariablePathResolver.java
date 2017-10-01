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

import org.slizaa.scanner.spi.content.support.DefaultVariablePath;

/**
 * <p>
 * Service interface. To provide an {@link IVariablePathResolver}, implement this interface and register an instance at
 * the OSGi service registry.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IVariablePathResolver {

  /**
   * <p>
   * Returns the resolved {@link DefaultVariablePath}.
   * </p>
   * 
   * @param variablePath
   * @return
   */
  String resolve(DefaultVariablePath variablePath);
}
