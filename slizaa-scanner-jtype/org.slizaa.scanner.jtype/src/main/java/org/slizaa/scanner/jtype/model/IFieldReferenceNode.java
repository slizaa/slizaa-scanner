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

import org.slizaa.scanner.core.spi.parser.model.INode;

public interface IFieldReferenceNode extends INode {

  public static final String OWNER_TYPE_NAME = "type_fqn";

  public static final String TYPE = "";

  public static final String STATIC = "static";
}
