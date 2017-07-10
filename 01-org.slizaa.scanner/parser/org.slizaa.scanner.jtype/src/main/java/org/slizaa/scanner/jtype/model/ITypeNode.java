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

import java.util.List;

import org.slizaa.scanner.api.model.INode;

public interface ITypeNode extends INode {

  public static final String ABSTRACT                 = "abstract";

  public static final String CLASS_VERSION            = "classVersion";

  public static final String DEPRECATED               = "deprecated";

  public static final String ACCESS_FLAGS             = "accessFlags";

  public static final String SIGNATURE                = "signature";

  public static final String FINAL                    = "final";

  public static final String VISIBILITY               = "visibility";

  public static final String STATIC                   = "static";

  public static final String INNER_CLASS              = "innerClass";

  public static final String INNER_CLASS_ACCESS_LEVEL = "innerClassAccessLevel";

  public static final String INNER_CLASS_ACCESS_FLAGS = "innerClassAccessFlags";

  public static final String OUTER_CLASSNAME          = "outerClassName";

  public static final String SOURCE_FILE_NAME         = "sourceFileName";

  String getFullyQualifiedName();

  String getName();

  String getClassVersion();

  String getAccessFlags();

  String getSignature();

  boolean isAbstract();

  boolean isDeprecated();

  boolean isFinal();

  AccessLevel getAccessLevel();

  boolean isInnerClass();

  AccessLevel getInnerClassAccessLevel();

  String getInnerClassAccessFlags();

  String getOuterClassName();

  List<IFieldNode> getFields();

  List<IMethodNode> getMethods();
}
