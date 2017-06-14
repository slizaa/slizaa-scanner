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
package org.slizaa.scanner.model.resource;

import org.slizaa.scanner.model.INode;

/**
 * <p>
 * Defines the interface for a module node bean.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IModuleNode extends INode {

  /** the property 'moduleName' */
  public String PROPERTY_MODULE_NAME     = "moduleName";

  /** the property 'moduleVersion' */
  public String PROPERTY_MODULE_VERSION  = "moduleVersion";

  /** - */
  public String PROPERTY_CONTENT_ENTRY_ID = "contentEntryId";

  /**
   * <p>
   * Returns the module name.
   * </p>
   * 
   * @return the name of the module.
   */
  String getModuleName();

  /**
   * <p>
   * Returns the module version.
   * </p>
   * 
   * @return the module version.
   */
  String getModuleVersion();
}
