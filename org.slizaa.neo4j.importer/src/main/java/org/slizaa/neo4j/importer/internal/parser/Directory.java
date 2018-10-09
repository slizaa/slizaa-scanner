/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.neo4j.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import org.slizaa.scanner.spi.contentdefinition.filebased.IFile;

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
  private List<IFile> _binaryResources;

  /** - */
  private List<IFile> _sourceResources;

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
    _binaryResources = new LinkedList<IFile>();
    _sourceResources = new LinkedList<IFile>();
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
  public void addBinaryResource(IFile resourceStandin) {
    _binaryResources.add(resourceStandin);
    _count++;
  }

  /**
   * <p>
   * </p>
   * 
   * @param resourceStandin
   */
  public void addSourceResource(IFile resourceStandin) {
    _sourceResources.add(resourceStandin);
    _count++;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public List<IFile> getBinaryResources() {
    return _binaryResources;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public List<IFile> getSourceResources() {
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
