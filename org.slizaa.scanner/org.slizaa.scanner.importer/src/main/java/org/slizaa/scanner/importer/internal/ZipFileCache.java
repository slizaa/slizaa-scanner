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
package org.slizaa.scanner.importer.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * <p>
 * A cache for {@link ZipFile} instances. The cache can be globally enabled or disabled.
 * </p>
 * 
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
public class ZipFileCache {

  /** the instance */
  private static ZipFileCache   _instance    = new ZipFileCache();

  /** the zip file cache */
  LoadingCache<String, ZipFile> _cache       = CacheBuilder.newBuilder().build(new CacheLoader<String, ZipFile>() {
                                               public ZipFile load(String key) {
                                                 return newZipFile(key);
                                               }
                                             });

  /** indicates whether the cache is active or not */
  private boolean               _cacheActive = false;

  /**
   * <p>
   * Return the singelton instance
   * </p>
   * 
   * @return the singleton instance
   */
  public static ZipFileCache instance() {
    return _instance;
  }

  /**
   * <p>
   * Activate the cache.
   * </p>
   */
  public synchronized void activateCache() {
    _cacheActive = true;
  }

  /**
   * <p>
   * Deactivate the cache. The cache is cleaned and all cached ZipFiles are closed.
   * </p>
   */
  public synchronized void deactivateCache() {

    //
    _cacheActive = false;

    //
    for (Entry<String, ZipFile> entry : _cache.asMap().entrySet()) {
      try {
        entry.getValue().close();
      } catch (Exception ex) {
        // ignore
      }
    }

    //
    _cache.invalidateAll();
  }

  /**
   * <p>
   * Get a {@link ZipFile} instance for the specified fileName
   * </p>
   * 
   * @param fileName
   * @return New or Cached ZipFile
   */
  public synchronized ZipFile getZipFile(String fileName) {

    //
    if (_cacheActive) {
      return _cache.getUnchecked(fileName);
    }

    //
    return newZipFile(fileName);
  }

  /**
   * <p>
   * Creates a new zip file for the specified file.
   * </p>
   * 
   * @param fileName
   *          the zip file name
   * @return the zip file
   */
  private ZipFile newZipFile(String fileName) {

    //
    checkNotNull(fileName);
    checkState(fileName.trim().length() > 0);

    //
    File file = new File(fileName);

    //
    try {
      return new ZipFile(file);
    } catch (ZipException ex) {
      throw new RuntimeException("Could not open ZipFile for '" + fileName + "': " + ex.getMessage(), ex);
    } catch (IOException ex) {
      throw new RuntimeException("Could not open ZipFile for '" + fileName + "': " + ex.getMessage(), ex);
    }
  }
}
