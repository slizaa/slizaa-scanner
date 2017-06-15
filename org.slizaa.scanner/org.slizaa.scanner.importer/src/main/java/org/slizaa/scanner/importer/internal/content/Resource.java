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
package org.slizaa.scanner.importer.internal.content;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slizaa.scanner.importer.content.IResource;
import org.slizaa.scanner.importer.content.IResourceIdentifier;
import org.slizaa.scanner.importer.internal.ZipFileCache;

/**
 * <p>
 * Default implementation of the interface {@link IProjectContentResource}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noextend This class is not intended to be extended by clients.
 */
public class Resource implements IResource, IResourceIdentifier {

  /** the root of the resource */
  private String _root;

  /** the path of the resource */
  private String _path;

  /**
   * <p>
   * Creates a new instance of type {@link Resource}.
   * </p>
   * 
   * @param root
   * @param path
   */
  public Resource(String root, String path) {
   checkNotNull(root);
   checkNotNull(path);

    _root = normalize(root);
    _path = path;
  }

  /**
   * {@inheritDoc}
   */
  public String getRoot() {
    return _root.toString();
  }

  /**
   * {@inheritDoc}
   */
  public String getPath() {
    return _path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDirectory() {
    int lastIndex = _path.lastIndexOf('/');
    return lastIndex != -1 ? _path.substring(0, lastIndex) : "";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    int lastIndex = _path.lastIndexOf('/');
    return lastIndex != -1 ? _path.substring(lastIndex + 1) : _path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getContent() {

    // jar file?
    if (getRoot().endsWith(".jar") || getRoot().endsWith(".zip")) {

      try {
        ZipFile zipFile = new ZipFile(new File(getRoot()));
        ZipEntry zipEntry = zipFile.getEntry(getPath());

        InputStream is = zipFile.getInputStream(zipEntry);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
          buffer.write(data, 0, nRead);
        }
        buffer.flush();

        byte[] result = buffer.toByteArray();

        is.close();
        zipFile.close();

        // return the result
        return result;

      } catch (Exception ex) {
        // TODO Auto-generated catch block
        ex.printStackTrace();
        throw new RuntimeException("Error while parsing '" + getRoot() + "' with path '" + getPath() + "': " + ex, ex);
      }
    }

    // get the root file
    File rootFile = new File(getRoot());

    //
    if (rootFile.isDirectory()) {

      try {

        File file = new File(rootFile, getPath());
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
          buffer.write(data, 0, nRead);
        }
        buffer.flush();

        byte[] result = buffer.toByteArray();

        is.close();
        buffer.close();

        //
        return result;

      } catch (Exception e) {
        throw new RuntimeException("FEHLER");
      }

    } else {
      throw new RuntimeException("FEHLER");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getTimestamp() {

    // jar file?
    if (getRoot().endsWith(".jar") || getRoot().endsWith(".zip")) {
      try {
        ZipFile zipFile = ZipFileCache.instance().getZipFile(getRoot());
        ZipEntry zipEntry = zipFile.getEntry(getPath());
        return zipEntry.getTime();
      } catch (Exception e) {
      }
    } else {
      //
      return new File(getRoot(), getPath()).lastModified();
    }

    //
    return -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + _path.hashCode();
    result = prime * result + _root.hashCode();
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(IResourceIdentifier.class.isAssignableFrom(obj.getClass())))
      return false;
    IResourceIdentifier other = (IResourceIdentifier) obj;
    if (!_path.equals(other.getPath()))
      return false;
    if (!_root.equals(other.getRoot()))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return this.getClass().getCanonicalName() + " [_root=" + _root + ", _path=" + _path + "]";
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private String normalize(String string) {
    return string.replace('\\', '/');
  }
}
