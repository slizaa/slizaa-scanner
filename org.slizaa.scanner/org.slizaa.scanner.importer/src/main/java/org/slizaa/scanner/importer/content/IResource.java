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
package org.slizaa.scanner.importer.content;

/**
 * <p>
 * Defines the common interface for a resource. Normally a resource is either a file or a entry in an archive file. A
 * resource has a path and a <code>timestamp</code>. The {@link IResource} interface also provides convenience methods
 * to access the name of the resource and the path of the containing directory.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IResource {

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
   * Returns the directory of the resource, e.g. <code>'org/example'</code>. Note that resource paths are always
   * slash-delimited ('/').
   * </p>
   * 
   * @return the directory of the resource
   */
  String getDirectory();

  /**
   * <p>
   * Returns the name of the resource, e.g. <code>'Test.java'</code>.
   * </p>
   * 
   * @return the name of the resource
   */
  String getName();

  /**
   * <p>
   * The 'last-changed' timestamp of the underlying resource.
   * </p>
   * 
   * @return the 'last-changed' timestamp of the underlying resource.
   */
  long getTimestamp();

  /**
   * <p>
   * Returns the content of this resource.
   * </p>
   * 
   * @return the content of this resource.
   */
  byte[] getContent();
}
