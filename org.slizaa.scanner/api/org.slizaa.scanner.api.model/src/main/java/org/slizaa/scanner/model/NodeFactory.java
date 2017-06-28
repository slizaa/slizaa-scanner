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
package org.slizaa.scanner.model;

import org.slizaa.scanner.model.internal.NodeBean;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class NodeFactory {

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public static IModifiableNode createNode() {
    return new NodeBean();
  }

  /**
   * <p>
   * </p>
   * 
   * @param id
   * @return
   */
  public static IModifiableNode createNode(long id) {
    return new NodeBean(id);
  }
}
