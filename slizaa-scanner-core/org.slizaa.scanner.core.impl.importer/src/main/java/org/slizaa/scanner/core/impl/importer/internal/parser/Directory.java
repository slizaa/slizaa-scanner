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
package org.slizaa.scanner.core.impl.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import org.slizaa.scanner.core.spi.contentdefinition.IResource;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Directory {

  /** - */
  private String          _path;

  /** - */
  private List<IResource> _binaryResources;

  /** - */
  private List<IResource> _sourceResources;

  /** - */
  private int             _count = 0;

  /**
   * <p>
   * Creates a new instance of type {@link Directory}.
   * </p>
   */
  public Directory(String path) {
   checkNotNull(path);

    //
    _path = path;

    //
    _binaryResources = new LinkedList<IResource>();
    _sourceResources = new LinkedList<IResource>();
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public String getPath() {
    return _path;
  }

  /**
   * <p>
   * </p>
   * 
   * @param resourceStandin
   */
  public void addBinaryResource(IResource resourceStandin) {
    _binaryResources.add(resourceStandin);
    _count++;
  }

  /**
   * <p>
   * </p>
   * 
   * @param resourceStandin
   */
  public void addSourceResource(IResource resourceStandin) {
    _sourceResources.add(resourceStandin);
    _count++;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public List<IResource> getBinaryResources() {
    return _binaryResources;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public List<IResource> getSourceResources() {
    return _sourceResources;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public int getCount() {
    return _count;
  }
}
