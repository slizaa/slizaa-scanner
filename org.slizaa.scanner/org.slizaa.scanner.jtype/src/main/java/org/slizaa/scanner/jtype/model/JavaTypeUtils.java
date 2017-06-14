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

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JavaTypeUtils {

  /**
   * <p>
   * </p>
   * 
   * @param fqn
   * @return
   */
  public static String getSimpleName(String fullyQualifiedName) {
    checkNotNull(fullyQualifiedName);

    return fullyQualifiedName.lastIndexOf('.') != -1 ? fullyQualifiedName
        .substring(fullyQualifiedName.lastIndexOf('.') + 1) : fullyQualifiedName;
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullQualifiedName
   * @return
   */
  public static boolean isLocalOrAnonymousTypeName(String fullQualifiedName) {
    checkNotNull(fullQualifiedName);

    return fullQualifiedName.matches(".*\\$\\d.*");
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullQualifiedName
   * @return
   */
  public static boolean isInnerTypeName(String fullQualifiedName) {
    checkNotNull(fullQualifiedName);

    return fullQualifiedName.matches(".*\\$.*");
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullQualifiedName
   * @return
   */
  public static String getEnclosingNonInnerTypeName(String fullQualifiedName) {
    checkNotNull(fullQualifiedName);

    // local or anonymous?
    if (isInnerTypeName(fullQualifiedName)) {

      String[] parts = fullQualifiedName.split("\\$");
      return parts[0];

    } else {
      return fullQualifiedName;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullQualifiedName
   * @return
   */
  public static String getEnclosingNonLocalAndNonAnonymousTypeName(String fullQualifiedName) {
    checkNotNull(fullQualifiedName);

    // local or anonymous?
    if (isLocalOrAnonymousTypeName(fullQualifiedName)) {

      String[] parts = fullQualifiedName.split("\\$\\d");
      return parts[0];

    } else {
      return fullQualifiedName;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param classFilePath
   * @return
   */
  public static String convertToFullyQualifiedName(String classFilePath) {
    return convertToFullyQualifiedName(classFilePath, ".class");
  }

  /**
   * <p>
   * </p>
   * 
   * @param classFilePath
   * @param postfix
   * @return
   */
  public static String convertToFullyQualifiedName(String classFilePath, String postfix) {

    String fullyQualifiedName = classFilePath.substring(0, classFilePath.length() - postfix.length());

    if (fullyQualifiedName.startsWith("/")) {
      fullyQualifiedName = fullyQualifiedName.substring(1);
    }

    fullyQualifiedName = fullyQualifiedName.replace('/', '.');

    return fullyQualifiedName;
  }

  /**
   * <p>
   * </p>
   * 
   * @param fullyQualifiedName
   * @return
   */
  public static String convertFromFullyQualifiedName(String fullyQualifiedName) {

    String result = fullyQualifiedName.replace('.', '/');

    return result + ".class";
  }
}
