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
 * Defines the interface for a group node bean.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IGroupNode extends INode  {

  /**
   * <p>
   * Returns the full path of the group, e.g. <code>core/mygroup'</code>. Note that group paths are
   * always slash-delimited ('/').
   * </p>
   * 
   * @return the full path of the group.
   */
  public String getPath();
}
