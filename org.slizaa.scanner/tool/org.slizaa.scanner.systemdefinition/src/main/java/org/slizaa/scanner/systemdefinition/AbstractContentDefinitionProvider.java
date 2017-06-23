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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IResource;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.internal.ContentDefinition;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinition;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * Superclass for all implementations of {@link IContentDefinitionProvider}
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractContentDefinitionProvider implements IContentDefinitionProvider {

  /** the (unique) id of the content definition provider */
  @Expose
  @SerializedName("id")
  private String                   _id;

  /** the containing system definition */
  private ISystemDefinition        _systemDefinition;

  /** the list of content definitions */
  private List<IContentDefinition> _contentDefinitions;

  /** indicates whether or not this provider has been initialized */
  private boolean                  _isInitialized;

  /** the internal counter (used for content definition ids) */
  private int                      _counter = 1;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractContentDefinitionProvider}.
   * </p>
   */
  public AbstractContentDefinitionProvider() {

    //
    _contentDefinitions = new LinkedList<IContentDefinition>();
  }

  /**
   * <p>
   * Creates a new instance of type {@link AbstractContentDefinitionProvider}.
   * </p>
   * 
   * @param provider
   */
  protected AbstractContentDefinitionProvider(AbstractContentDefinitionProvider provider) {
    this();

    //
    checkNotNull(provider);

    //
    _id = provider._id;
    _systemDefinition = provider._systemDefinition;
    _counter = provider._counter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final String getId() {
    return _id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<IContentDefinition> getContentDefinitions() {

    //
    initializeProjectContent(null);

    //
    return Collections.unmodifiableList(_contentDefinitions);
  }

  /**
   * <p>
   * </p>
   * 
   * @param id
   */
  public final void setContentDefinitionProviderId(String id) {
    _id = id;
  }

  /**
   * <p>
   * </p>
   * 
   * @param progressMonitor
   * @return
   */
  public final void initializeProjectContent(IProgressMonitor progressMonitor) {

    if (!_isInitialized) {

      onInitializeProjectContent(progressMonitor);

      _isInitialized = true;
    }
  }

  /**
   * @param progressMonitor
   */
  protected abstract void onInitializeProjectContent(IProgressMonitor progressMonitor);

  /**
   * <p>
   * </p>
   */
  public void prepare() {
    // empty implementation
  }

  /**
   * <p>
   * </p>
   */
  protected void clearFileBasedContents() {
    _isInitialized = false;
    _contentDefinitions.clear();
  }

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   */
  public void setSystemDefinition(ISystemDefinition systemDefinition) {
    _systemDefinition = systemDefinition;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public ISystemDefinition getSystemDefinition() {
    return _systemDefinition;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public boolean hasSystemDefinition() {
    return _systemDefinition != null;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_id == null) ? 0 : _id.hashCode());
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
    AbstractContentDefinitionProvider other = (AbstractContentDefinitionProvider) obj;
    if (_id == null) {
      if (other._id != null)
        return false;
    } else if (!_id.equals(other._id))
      return false;
    return true;
  }

  /**
   * <p>
   * </p>
   * 
   * @param contentName
   * @param contentVersion
   * @param binaryPaths
   * @param sourcePaths
   * @param analyzeMode
   * @return
   * @throws CoreException
   */
  protected IContentDefinition createFileBasedContentDefinition(String contentName, String contentVersion,
      VariablePath[] binaryPaths, VariablePath[] sourcePaths, AnalyzeMode analyzeMode) {

    //
    // checkProjectSet();

    // asserts
    checkNotNull(contentName);
    checkNotNull(contentVersion);
    checkNotNull(binaryPaths);
    checkNotNull(analyzeMode);

    ContentDefinition result = new ContentDefinition(this);

    result.setAnalyzeMode(analyzeMode);

    result.setId(getId() + "-" + _counter++);
    result.setName(contentName);
    result.setVersion(contentVersion);

    for (VariablePath binaryPath : binaryPaths) {
      result.addRootPath(binaryPath, ResourceType.BINARY);
    }

    if (sourcePaths != null) {
      for (VariablePath sourcePath : sourcePaths) {
        result.addRootPath(sourcePath, ResourceType.SOURCE);
      }
    }

    // initialize the result
    result.initialize();

    //
    _contentDefinitions.add(result);

    //
    return result;
  }

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   * @param root
   * @param path
   * @param type
   */
  protected void handleResourceAdded(IContentDefinition contentDefinition, String root, String path, ResourceType type) {

    checkNotNull(root);
    checkNotNull(path);
    checkNotNull(type);

    checkNotNull(contentDefinition);
    checkState(contentDefinition instanceof ContentDefinition,
        "IContentDefinition must be instance of type ContentDefinition.");

    //
    IResource resource = ((ContentDefinition) contentDefinition).createNewResource(root, path, type);

    // fire event
    if (hasSystemDefinition()) {

      //
      ((SystemDefinition) getSystemDefinition()).fireContentChangedEvent(new ResourceChangedEvent(
          getSystemDefinition(), contentDefinition, resource, ResourceChangedEvent.Type.ADDED));
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param contentEntry
   * @param resource
   * @param type
   */
  protected void handleResourceRemoved(IContentDefinition contentDefinition, IResource resource, ResourceType type) {

    checkNotNull(resource);
    checkNotNull(type);

    checkNotNull(contentDefinition);
    checkState(contentDefinition instanceof ContentDefinition,
        "IContentDefinition must be instance of type ContentDefinition.");

    //
    ((ContentDefinition) contentDefinition).removeResource(resource, type);

    // fire event
    if (hasSystemDefinition()) {

      //
      ((SystemDefinition) getSystemDefinition()).fireContentChangedEvent(new ResourceChangedEvent(
          getSystemDefinition(), contentDefinition, resource, ResourceChangedEvent.Type.REMOVED));
    }
  }

  // protected void handleResourceModified(IProjectContentEntry contentEntry, IProjectContentResource contentResource) {
  //
  // //
  // getInterceptor().resourceContentModified(contentEntry, contentResource);
  //
  // //
  // fireProjectContentChangedEvent(new BundleMakerProjectContentChangedEvent(getBundleMakerProject(),
  // BundleMakerProjectContentChangedEvent.Type.MODIFIED, contentResource));
  // }

  // /**
  // * <p>
  // * </p>
  // */
  // private void checkProjectSet() {
  // checkNotNull(_bundleMakerProject, "BundleMaker project has not been set.");
  // }

  //
  // /**
  // * <p>
  // * </p>
  // *
  // * @return
  // */
  // // TODO MOVE
  // private IHandleChangedProjectContentInterceptor getInterceptor() {
  //
  // //
  // if (_interceptor == null) {
  //
  // //
  // _interceptor = new IHandleChangedProjectContentInterceptor() {
  //
  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void resourceContentModified(IProjectContentEntry contentEntry, IProjectContentResource contentResource) {
  //
  // //
  // try {
  // parse(contentEntry, contentResource.adaptAs(IParsableResource.class));
  // } catch (CoreException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // }
  //
  // /**
  // * {@inheritDoc}
  // */
  // @Override
  // public void resourceContentAdded(IProjectContentEntry contentEntry, IProjectContentResource contentResource) {
  //
  // //
  // try {
  // parse(contentEntry, contentResource.adaptAs(IParsableResource.class));
  // } catch (CoreException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // }
  //
  // /**
  // * @param contentEntry
  // * @param contentResource
  // * @throws CoreException
  // */
  // private void parse(IProjectContentEntry contentEntry, IParsableResource contentResource) throws CoreException {
  // IParserService.Factory.getParserService().parseResource(contentEntry, contentResource, true);
  // }
  // };
  // }
  //
  // //
  // return _interceptor;
  // }
}
