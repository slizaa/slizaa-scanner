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
package org.slizaa.scanner.importer.spi.content;

public interface IResourceIdentifier {

  /**
   * <p>
   * Returns the root directory or archive file that contains the resource (e.g. <code>'c:/dev/classes.zip'</code> or
   * <code>'c:/dev/source'</code>). Note that resource paths are always slash-delimited ('/').
   * </p>
   * 
   * @return the root directory or archive file that contains the resource.
   */
  public String getRoot();

  /**
   * <p>
   * Returns the full path of the resource, e.g. <code>'org/example/Test.java'</code>. Note that resource paths are
   * always slash-delimited ('/').
   * </p>
   * <p>
   * The result of this method is equivalent to <code>'getDirectory() + "/" + getName()'</code>.
   * </p>
   * 
   * @return the full path of the resource.
   */
  public String getPath();

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Helper {

    /**
     * <p>
     * </p>
     * 
     * @param resId
     * @return
     */
    public static int hashCode(IResourceIdentifier resId) {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((resId.getPath() == null) ? 0 : resId.getPath().hashCode());
      result = prime * result + ((resId.getRoot() == null) ? 0 : resId.getRoot().hashCode());
      return result;
    }

    /**
     * <p>
     * </p>
     * 
     * @param resId
     * @param obj
     * @return
     */
    public static boolean equals(IResourceIdentifier resId, Object obj) {
      if (resId == obj)
        return true;
      if (obj == null)
        return false;
      if (!(IResourceIdentifier.class.isAssignableFrom(obj.getClass())))
        return false;
      IResourceIdentifier other = (IResourceIdentifier) obj;
      if (resId.getPath() == null) {
        if (other.getPath() != null)
          return false;
      } else if (!resId.getPath().equals(other.getPath()))
        return false;
      if (resId.getRoot() == null) {
        if (other.getRoot() != null)
          return false;
      } else if (!resId.getRoot().equals(other.getRoot()))
        return false;
      return true;
    }
  }
}
