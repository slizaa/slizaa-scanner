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

public class JavaUtils {

  /**
   * <p>
   * </p>
   *
   * @param path
   * @return
   */
  public static boolean isValidJavaPackage(String path) {

    checkNotNull(path);

    //
    String[] elements = path.split("/");

    //
    for (int i = 0; i < elements.length - 1; i++) {

      String element = elements[i];

      if (!isValidJavaIdentifier(element)) {
        return false;
      }
    }

    //
    return true;
  }

  /**
   * <p>
   * </p>
   *
   * @param directory
   * @return
   */
  public static String getPackageNameFromDirectory(String directory) {
    return directory.replace('/', '.');
  }

  /**
   * <p>
   * </p>
   * 
   * @param s
   * @return
   */
  public final static boolean isValidJavaIdentifier(String s) {

    // an empty or null string cannot be a valid identifier
    if (s == null || s.length() == 0) {
      return false;
    }

    char[] c = s.toCharArray();
    if (!Character.isJavaIdentifierStart(c[0])) {
      return false;
    }

    for (int i = 1; i < c.length; i++) {
      if (!Character.isJavaIdentifierPart(c[i])) {
        return false;
      }
    }

    return true;
  }
}
