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
package org.slizaa.scanner.jtype.model;

public interface IMethodNode {

  public static final String NATIVE          = "native";

  public static final String FQN             = "fqn";

  public static final String NAME            = "name";

  public static final String FINAL           = "final";

  public static final String STATIC          = "static";

  public static final String VISIBILITY      = "visibility";

  public static final String ABSTRACT        = "abstract";

  public static final String SYNTHETIC       = "synthetic";

  // TODO Relationship
  public static final String PARAMETER_INDEX = "parameter_index";
}
