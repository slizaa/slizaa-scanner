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
package org.slizaa.scanner.systemdefinition;

import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.spi.content.support.DefaultVariablePath;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinition;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FileBasedContentDefinitionProvider extends AbstractContentDefinitionProvider implements
    ITempDefinitionProvider {

  /** the name of this entry */
  @Expose
  @SerializedName("name")
  private String               _name;

  /** the version of this entry */
  @Expose
  @SerializedName("version")
  private String               _version;

  /** the binary paths */
  @Expose
  @SerializedName("binary-paths")
  protected List<DefaultVariablePath> _binaryPaths;

  /** the source paths */
  @Expose
  @SerializedName("source-paths")
  private List<DefaultVariablePath>   _sourcePaths;

  /** the analyze mode of this entry */
  @Expose
  @SerializedName("analyse")
  private AnalyzeMode          _analyzeMode;

  /**
   * <p>
   * Creates a new instance of type {@link FileBasedContentDefinitionProvider}.
   * </p>
   * 
   * @param name
   * @param version
   * @param analyzeMode
   */
  public FileBasedContentDefinitionProvider(String name, String version, AnalyzeMode analyzeMode) {
    this();

    setName(name);
    setVersion(version);
    setAnalyzeMode(analyzeMode);
  }

  /**
   * <p>
   * Creates a new instance of type {@link FileBasedContentDefinitionProvider}.
   * </p>
   */
  public FileBasedContentDefinitionProvider() {
    _binaryPaths = new LinkedList<DefaultVariablePath>();
    _sourcePaths = new LinkedList<DefaultVariablePath>();
  }

  /**
   * <p>
   * Creates a new instance of type {@link FileBasedContentDefinitionProvider}.
   * </p>
   * 
   * @param original
   */
  FileBasedContentDefinitionProvider(FileBasedContentDefinitionProvider original) {
    super(original);

    _name = original._name;
    _version = original._version;
    _analyzeMode = original._analyzeMode;

    _binaryPaths = new LinkedList<DefaultVariablePath>(original._binaryPaths);
    _sourcePaths = new LinkedList<DefaultVariablePath>(original._sourcePaths);
  }

  @Override
  public ITempDefinitionProvider copyInstance() {
    return new FileBasedContentDefinitionProvider(this);
  }

  @Override
  public void merge(ITempDefinitionProvider provider) {
    checkState(provider != null, "Provider has to be set.");
    checkState(provider instanceof FileBasedContentDefinitionProvider, String.format(
        "Provider has to be instance of %s, but is instance of %s", FileBasedContentDefinitionProvider.class.getName(),
        provider.getClass().getName()));

    FileBasedContentDefinitionProvider p = (FileBasedContentDefinitionProvider) provider;

    _name = p._name;
    _version = p._version;
    _binaryPaths.clear();
    _binaryPaths.addAll(p._binaryPaths);
    _sourcePaths.clear();
    _sourcePaths.addAll(p._sourcePaths);
    _analyzeMode = p._analyzeMode;
  }

  /**
   * <p>
   * Set the name of the module that is represented by this {@link FileBasedContentDefinitionProvider} to the given
   * value.
   * </p>
   * 
   * @param name
   */
  public void setName(String name) {
    _name = name;

    //
    providerChanged();
  }

  /**
   * <p>
   * Returns the name of this {@link FileBasedContentDefinitionProvider}.
   * </p>
   * 
   * @return
   */
  public String getName() {
    return _name;
  }

  /**
   * <p>
   * Set the Version of this IFileBasedContent to the given value
   * </p>
   * 
   * @param version
   */
  public void setVersion(String version) {
    _version = version;

    //
    providerChanged();
  }

  /**
   * <p>
   * Returns the version of this {@link FileBasedContentDefinitionProvider}.
   * </p>
   * 
   * @return the version of this {@link FileBasedContentDefinitionProvider}.
   */
  public String getVersion() {
    return _version;
  }

  /**
   * <p>
   * Sets the {@link AnalyzeMode} of this {@link FileBasedContentDefinitionProvider}.
   * </p>
   * 
   * @param analyzeMode
   *          the {@link AnalyzeMode} of this {@link FileBasedContentDefinitionProvider}.
   */
  public void setAnalyzeMode(AnalyzeMode analyzeMode) {
    _analyzeMode = analyzeMode;

    //
    providerChanged();
  }

  /**
   * <p>
   * Returns the {@link AnalyzeMode} of this {@link FileBasedContentDefinitionProvider}.
   * </p>
   * 
   * @return the {@link AnalyzeMode} of this {@link FileBasedContentDefinitionProvider}.
   */
  public AnalyzeMode getAnalyzeMode() {
    return _analyzeMode;
  }

  /**
   * <p>
   * Sets the given binary root paths.
   * </p>
   * 
   * @param binaryRootPaths
   */
  public void setBinaryPaths(String[] binaryRootPaths) {

    //
    _binaryPaths.clear();
    for (String path : binaryRootPaths) {
      _binaryPaths.add(new DefaultVariablePath(path));
    }

    //
    providerChanged();
  }

  /**
   * <p>
   * Returns the binary paths.
   * </p>
   * 
   * @return the binary paths.
   */
  public List<DefaultVariablePath> getBinaryPaths() {
    return _binaryPaths;
  }

  /**
   * <p>
   * Sets the given source root paths.
   * </p>
   * 
   * @param sourceRootPaths
   */
  public void setSourcePaths(String[] sourceRootPaths) {

    //
    _sourcePaths.clear();
    for (String path : sourceRootPaths) {
      _sourcePaths.add(new DefaultVariablePath(path));
    }

    //
    providerChanged();
  }

  /**
   * <p>
   * Returns the source paths.
   * </p>
   * 
   * @return
   */
  public List<DefaultVariablePath> getSourcePaths() {
    return _sourcePaths;
  }

  /**
   * <p>
   * </p>
   * 
   * @param path
   * @param type
   */
  public void addRootPath(File path, ResourceType type) {
    addRootPath(new DefaultVariablePath(path.getAbsolutePath()), type);
  }

  /**
   * <p>
   * </p>
   * 
   * @param path
   * @param type
   */
  public void addRootPath(String path, ResourceType type) {
    addRootPath(new DefaultVariablePath(path), type);
  }

  /**
   * <p>
   * Adds the given path as a root path.
   * </p>
   * 
   * @param path
   * @param type
   */
  public void addRootPath(DefaultVariablePath path, ResourceType type) {

    //
    if (ResourceType.BINARY.equals(type)) {
      _binaryPaths.add(path);
    }

    //
    else if (ResourceType.SOURCE.equals(type)) {
      _sourcePaths.add(path);
    }

    //
    providerChanged();
  }

  /**
   * <p>
   * Removes the given root path.
   * </p>
   * 
   * @param path
   * @param contentType
   */
  public void removeRootPath(DefaultVariablePath path, ResourceType type) {

    //
    if (ResourceType.BINARY.equals(type)) {
      _binaryPaths.remove(path);
    }

    //
    else if (ResourceType.SOURCE.equals(type)) {
      _sourcePaths.remove(path);
    }

    //
    providerChanged();
  }

  /**
   * <p>
   * Returns <code>true</code> if the analyze mode is either <code>AnalyzeMode.BINARIES_ONLY</code> or
   * <code>AnalyzeMode.BINARIES_AND_SOURCES</code>.
   * </p>
   * 
   * @return <code>true</code> if the analyze mode is either <code>AnalyzeMode.BINARIES_ONLY</code> or
   *         <code>AnalyzeMode.BINARIES_AND_SOURCES</code>.
   */
  public boolean isAnalyze() {
    return _analyzeMode == AnalyzeMode.BINARIES_ONLY || _analyzeMode == AnalyzeMode.BINARIES_AND_SOURCES;
  }

  /**
   * <p>
   * Returns the (one and only) {@link IContentDefinition}.
   * </p>
   * 
   * @return
   * @throws CoreException
   */
  public IContentDefinition getFileBasedContent() {
    return getContentDefinitions().get(0);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((_analyzeMode == null) ? 0 : _analyzeMode.hashCode());
    result = prime * result + ((_binaryPaths == null) ? 0 : _binaryPaths.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_sourcePaths == null) ? 0 : _sourcePaths.hashCode());
    result = prime * result + ((_version == null) ? 0 : _version.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    FileBasedContentDefinitionProvider other = (FileBasedContentDefinitionProvider) obj;
    if (_analyzeMode != other._analyzeMode)
      return false;
    if (_binaryPaths == null) {
      if (other._binaryPaths != null)
        return false;
    } else if (!_binaryPaths.equals(other._binaryPaths))
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
   */
  protected void providerChanged() {

    // clear the file based content
    clearFileBasedContents();

    // fire project description changed event
    if (hasSystemDefinition()) {
      ((SystemDefinition) getSystemDefinition()).fireSystemDefinitionChangedEvent(new SystemDefinitionChangedEvent());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onInitializeProjectContent(IProgressMonitor progressMonitor) {

    //
    try {

      //
      createFileBasedContentDefinition(_name, _version, convert(_binaryPaths), convert(_sourcePaths), _analyzeMode);

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param variablePaths
   * @return
   * @throws CoreException
   */
  private DefaultVariablePath[] convert(List<DefaultVariablePath> variablePaths) {
    return variablePaths != null ? variablePaths.toArray(new DefaultVariablePath[0]) : new DefaultVariablePath[0];
  }
}
