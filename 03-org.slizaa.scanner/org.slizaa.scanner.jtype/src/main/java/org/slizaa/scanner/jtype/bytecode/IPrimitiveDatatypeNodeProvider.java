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
package org.slizaa.scanner.jtype.bytecode;

import org.slizaa.scanner.api.model.INode;

/**
 * <p>
 * The {@link IPrimitiveDatatypeNodeProvider} provides the {@link INode NodeBeans} for the primitive datatypes (e.g.
 * <code>int</code>, <code>long</code>).
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IPrimitiveDatatypeNodeProvider {

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeByte();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeShort();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeInt();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeLong();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeFloat();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeDouble();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeChar();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getPrimitiveDatatypeBoolean();

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public INode getVoid();
}
