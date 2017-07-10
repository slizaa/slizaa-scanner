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
package org.slizaa.scanner.systemdefinition.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.CoreException;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IResource;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.VariablePath;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class ContentDefinition implements IContentDefinition {

  /** the empty resource standin set */
  private static final List<IResource>    EMPTY_RESOURCE_SET = Collections
                                                                 .unmodifiableList(new LinkedList<IResource>());

  /** - */
  private static final List<VariablePath> EMPTY_ROOTPATH_SET = Collections
                                                                 .unmodifiableList(new LinkedList<VariablePath>());

  /** the internal identifier of this content entry */
  private String                          _id;

  /** the name of this entry */
  private String                          _name;

  /** the version of this entry */
  private String                          _version;

  /** the analyze mode of this entry */
  private AnalyzeMode                     _analyze;

  /** the binary pathes */
  protected List<VariablePath>            _binaryPaths;

  /** the source pathes */
  private List<VariablePath>              _sourcePaths;

  /** indicates that the content has been initialized */
  private boolean                         _isInitialized;

  /** the set of binary resource standins */
  private Map<String, IResource>          _binaryResources;

  /** the set of source resource standins */
  private Map<String, IResource>          _sourceResources;

  /** the bundle maker project content provider */
  private IContentDefinitionProvider      _provider;

  /**
   * <p>
   * Creates a new instance of type {@link ContentDefinition}.
   * </p>
   */
  public ContentDefinition(IContentDefinitionProvider provider) {

    // set the provider
    _provider = provider;

    //
    setAnalyzeMode(AnalyzeMode.BINARIES_ONLY);

    //
    _binaryPaths = new LinkedList<VariablePath>();
  }

  /**
   * {@inheritDoc}
   */
  public IContentDefinitionProvider getProvider() {
    return _provider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId() {
    return _id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return _name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVersion() {
    return _version;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isAnalyze() {
    return _analyze.isAnalyze();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AnalyzeMode getAnalyzeMode() {
    return _analyze;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Collection<IResource> getBinaryResources() {
    assertIsInitialized();
    return _binaryResources != null ? Collections.unmodifiableCollection(_binaryResources.values())
        : EMPTY_RESOURCE_SET;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Collection<IResource> getSourceResources() {
    assertIsInitialized();
    return _sourceResources != null ? Collections.unmodifiableCollection(_sourceResources.values())
        : EMPTY_RESOURCE_SET;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<IResource> getResources(ResourceType type) {
    assertIsInitialized();

    switch (type) {
    case BINARY: {
      return getBinaryResources();
    }
    case SOURCE: {
      return getSourceResources();
    }
    default: {
      return null;
    }
    }
  }

  /**
   * {@inheritDoc}
   */
  public IResource getResource(String path, ResourceType type) {
    assertIsInitialized();

    switch (type) {
    case BINARY: {
      return binaryResourcesMap().get(path);
    }
    case SOURCE: {
      return sourceResourcesMap().get(path);
    }
    default: {
      return null;
    }
    }
  }

  /**
   * <p>
   * Returns <code>true</code> if the content has been initialized yet.
   * </p>
   * 
   * @return <code>true</code> if the content has been initialized yet.
   */
  protected boolean isInitialized() {
    return _isInitialized;
  }

  /**
   * <p>
   * Sets the id of this content entry.
   * </p>
   * 
   * @param id
   *          the id of this content entry.
   */
  public void setId(String id) {
    checkNotNull(id);
    _id = id;
  }

  /**
   * <p>
   * Sets the name of this content entry.
   * </p>
   * 
   * @param name
   *          the name of this content entry.
   */
  public void setName(String name) {
    checkNotNull(name);
    _name = name;

    // fireProjectDescriptionChangeEvent();
  }

  /**
   * <p>
   * Sets the version of this content entry.
   * </p>
   * 
   * @param name
   *          the version of this content entry.
   */
  public void setVersion(String version) {
    checkNotNull(version);
    _version = version;

    // fireProjectDescriptionChangeEvent();
  }

  /**
   * <p>
   * Sets the {@link AnalyzeMode} of this content entry.
   * </p>
   * 
   * @param name
   *          the {@link AnalyzeMode} of this content entry.
   */
  public void setAnalyzeMode(AnalyzeMode analyzeMode) {
    checkNotNull(analyzeMode, "Paramter 'analyzeMode' must not be null");

    //
    _analyze = analyzeMode;

    // fireProjectDescriptionChangeEvent();
  }

  /**
   * <p>
   * Initializes this content entry.
   * </p>
   * 
   * @throws CoreException
   */
  public final void initialize() {

    // return if content already is initialized
    if (isInitialized()) {
      return;
    }

    //
    // add the binary resources
    if (_binaryPaths != null) {
      for (VariablePath root : _binaryPaths) {
        for (String filePath : getAllChildren(new File(root.getResolvedPath()))) {

          //
          createNewResource(root.getResolvedPath().toString(), filePath, ResourceType.BINARY);
        }
      }
    }

    // add the source resources
    if (_sourcePaths != null) {
      for (VariablePath root : _sourcePaths) {
        for (String filePath : getAllChildren(new File(root.getResolvedPath()))) {

          //
          createNewResource(root.getResolvedPath().toString(), filePath, ResourceType.SOURCE);
        }
      }
    }

    // set initialized
    _isInitialized = true;
  }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param contentId
  // * @param root
  // * @param path
  // * @param type
  // * @return
  // */
  // protected IResourceStandinNEW createNewResourceStandin(String contentId, String root, String path, ResourceType
  // type,
  // boolean analyzeReferences) {
  //
  // Assert.isNotNull(contentId);
  // Assert.isNotNull(root);
  // Assert.isNotNull(path);
  // Assert.isNotNull(type);
  //
  // //
  // IResourceStandinNEW resourceStandin = getResourceFactory().createResourceStandin(contentId, root, path);
  // resourceStandin.setAnalyzeReferences(analyzeReferences);
  //
  // // add the resource
  // switch (type) {
  // case BINARY: {
  // _projectDescription.addBinaryResource(resourceStandin);
  // binaryResourcesMap().put(path, resourceStandin);
  // break;
  // }
  // case SOURCE: {
  // _projectDescription.addSourceResource(resourceStandin);
  // sourceResourcesMap().put(path, resourceStandin);
  // break;
  // }
  // default:
  // break;
  // }
  //
  // //
  // return resourceStandin;
  // }

  // protected void fireProjectDescriptionChangeEvent() {
  //
  // if (_notifyChanges && _provider instanceof AbstractProjectContentProvider) {
  // ((AbstractProjectContentProvider) _provider).fireProjectDescriptionChangedEvent();
  // }
  //
  // }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private Map<String, IResource> binaryResourcesMap() {

    //
    if (_binaryResources == null) {
      _binaryResources = new HashMap<String, IResource>();
    }

    //
    return _binaryResources;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private Map<String, IResource> sourceResourcesMap() {

    //
    if (_sourceResources == null) {
      _sourceResources = new HashMap<String, IResource>();
    }

    //
    return _sourceResources;
  }

  /**
   * {@inheritDoc}
   */
  public List<VariablePath> getBinaryRootPaths() {
    return Collections.unmodifiableList(_binaryPaths);
  }

  /**
   * {@inheritDoc}
   */
  public List<VariablePath> getSourceRootPaths() {
    return _sourcePaths != null ? _sourcePaths : EMPTY_ROOTPATH_SET;
  }

  /**
   * <p>
   * </p>
   * 
   * @param rootPath
   * @param type
   */
  public void addRootPath(VariablePath rootPath, ResourceType type) {
    checkNotNull(rootPath);
    checkNotNull(type);

    //
    if (type.equals(ResourceType.BINARY)) {
      _binaryPaths.add(rootPath);
    } else if (type.equals(ResourceType.SOURCE)) {
      sourcePaths().add(rootPath);
    }

    // fireProjectDescriptionChangeEvent();
  }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param rootPath
  // * @param type
  // */
  // public void removeRootPath(IVariablePath rootPath, ResourceType type) {
  // Assert.isNotNull(rootPath);
  // Assert.isNotNull(type);
  //
  // //
  // if (type.equals(ResourceType.BINARY)) {
  // _binaryPaths.remove(rootPath);
  // } else if (type.equals(ResourceType.SOURCE)) {
  // _sourcePaths.remove(rootPath);
  // }
  //
  // fireProjectDescriptionChangeEvent();
  // }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param binaryRootPaths
  // */
  // public void setBinaryPaths(String[] binaryRootPaths) {
  // Assert.isNotNull(binaryRootPaths);
  //
  // _binaryPaths.clear();
  //
  // for (String path : binaryRootPaths) {
  // _binaryPaths.add(new VariablePath(path));
  // }
  //
  // fireProjectDescriptionChangeEvent();
  //
  // }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param sourceRootPaths
  // */
  // public void setSourcePaths(String[] sourceRootPaths) {
  // Assert.isNotNull(sourceRootPaths);
  //
  // sourcePaths().clear();
  //
  // for (String path : sourceRootPaths) {
  // sourcePaths().add(new VariablePath(path));
  // }
  //
  // fireProjectDescriptionChangeEvent();
  // }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(getClass().getSimpleName());
    builder.append(" [_binaryPaths=");
    builder.append(_binaryPaths);
    builder.append(", _sourcePaths=");
    builder.append(_sourcePaths);
    builder.append(", getId()=");
    builder.append(getId());
    builder.append(", getName()=");
    builder.append(getName());
    builder.append(", getVersion()=");
    builder.append(getVersion());
    builder.append(", isAnalyze()=");
    builder.append(isAnalyze());
    builder.append("]");
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_analyze == null) ? 0 : _analyze.hashCode());
    result = prime * result + ((_binaryPaths == null) ? 0 : _binaryPaths.hashCode());
    result = prime * result + ((_id == null) ? 0 : _id.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_sourcePaths == null) ? 0 : _sourcePaths.hashCode());
    result = prime * result + ((_version == null) ? 0 : _version.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ContentDefinition other = (ContentDefinition) obj;
    if (_analyze != other._analyze)
      return false;
    if (_binaryPaths == null) {
      if (other._binaryPaths != null)
        return false;
    } else if (!_binaryPaths.equals(other._binaryPaths))
      return false;
    if (_id == null) {
      if (other._id != null)
        return false;
    } else if (!_id.equals(other._id))
      return false;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    if (_sourcePaths == null) {
      if (other._sourcePaths != null)
        return false;
    } else if (!_sourcePaths.equals(other._sourcePaths))
      return false;
    if (_version == null) {
      if (other._version != null)
        return false;
    } else if (!_version.equals(other._version))
      return false;
    return true;
  }

  /**
   * <p>
   * </p>
   * 
   * @param root
   * @param path
   * @param type
   * @return
   */
  public IResource createNewResource(String root, String path, ResourceType type) {

    //
    Map<String, IResource> resourcesMap = type.equals(ResourceType.BINARY) ? binaryResourcesMap()
        : sourceResourcesMap();

    //
    if (!resourcesMap.containsKey(path)) {

      Resource result = new Resource(root, path);

      // add the resource
      switch (type) {
      case BINARY: {
        binaryResourcesMap().put(path, result);
        break;
      }
      case SOURCE: {
        sourceResourcesMap().put(path, result);
        break;
      }
      default:
        break;
      }

      // return resource
      return new Resource(root, path);

    } else {
      //
      System.out.println(String.format("DUPLICATE RESOURCE IN ENTRY '%s': '%s'", getId(), path));
      if (_isInitialized) {
        return getResource(path, type);
      } else {
        return null;
      }
    }
  }

  public void removeResource(IResource resource, ResourceType type) {

    //
    // add the resource
    switch (type) {
    case BINARY: {
      binaryResourcesMap().remove(resource.getPath());
      break;
    }
    case SOURCE: {
      sourceResourcesMap().remove(resource.getPath());
      break;
    }
    default:
      break;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  private List<VariablePath> sourcePaths() {

    // lazy initialization
    if (_sourcePaths == null) {
      _sourcePaths = new LinkedList<VariablePath>();
    }

    // return the source paths
    return _sourcePaths;
  }

  /**
   * <p>
   * </p>
   */
  private void assertIsInitialized() {
    if (!_isInitialized) {
      checkState(false, String.format("ProjectContentEntry '%s' has to be initialized.", toString()));
    }
  }

  /**
   * <p>
   * Returns a list with all children of the given file. If the file is a directory, all file paths contained in all
   * folders and sub-folders are returned. If the file is a zip file or a jar archive, the paths of all entries are
   * returned.
   * </p>
   * 
   * @param file
   *          the file
   * @return the list of paths
   */
  public static List<String> getAllChildren(File file) {

    //
    if (file.isDirectory()) {

      List<String> result = new LinkedList<String>();
      getAllChildren(file, file, result);
      return result;
    }

    //
    else if (file.isFile() && (file.getName().endsWith(".zip") || file.getName().endsWith(".jar"))) {

      try {
        ZipFile zipFile = new ZipFile(file);

        Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

        List<String> result = new LinkedList<String>();

        while (enumeration.hasMoreElements()) {
          ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
          if (!zipEntry.isDirectory()) {
            String name = zipEntry.getName();
            name = name.replace('\\', '/');
            result.add(name);
          }
        }

        zipFile.close();

        return result;

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    // throw the core exception
    throw new RuntimeException(String.format("File '%s' does not exist.", file.getAbsolutePath()));
  }

  /**
   * <p>
   * </p>
   * 
   * @param root
   * @param directory
   * @return
   */
  private static void getAllChildren(File root, File directory, List<String> content) {

    int length = root.getAbsolutePath().length();

    //
    for (File child : directory.listFiles()) {

      if (child.isFile()) {

        String entry = child.getAbsolutePath().substring(length + 1);

        entry = entry.replace("\\", "/");

        content.add(entry);

      } else if (child.isDirectory()) {
        getAllChildren(root, child, content);
      }
    }
  }
}
