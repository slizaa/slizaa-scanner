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
package org.slizaa.scanner.core.contentdefinition;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IContentDefinitionProvider;
import org.slizaa.scanner.spi.content.ResourceType;

/**
 * <p>
 * Superclass for all implementations of {@link ITempDefinitionProvider}
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractContentDefinitionProvider implements IContentDefinitionProvider {

  /** the list of content definitions */
  private List<IContentDefinition> _contentDefinitions;

  /** indicates whether or not this provider has been initialized */
  private boolean                  _isInitialized;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractContentDefinitionProvider}.
   * </p>
   * 
   * @param contentDefinitionProvider
   */
  public AbstractContentDefinitionProvider() {

    //
    _contentDefinitions = new LinkedList<IContentDefinition>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<IContentDefinition> getContentDefinitions() {

    //
    initializeProjectContent();

    //
    return Collections.unmodifiableList(_contentDefinitions);
  }

  /**
   * <p>
   * </p>
   * 
   * @param progressMonitor
   * @return
   */
  private final void initializeProjectContent() {

    if (!_isInitialized) {

      onInitializeProjectContent();

      _isInitialized = true;
    }
  }

  /**
   * <p>
   * </p>
   */
  protected abstract void onInitializeProjectContent();

  /**
   * <p>
   * </p>
   */
  protected void clearContentDefinitions() {
    _isInitialized = false;
    _contentDefinitions.clear();
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
      File[] binaryPaths, File[] sourcePaths, AnalyzeMode analyzeMode) {

    // asserts
    checkNotNull(contentName);
    checkNotNull(contentVersion);
    checkNotNull(binaryPaths);
    checkNotNull(analyzeMode);

    FileBasedContentDefinition result = new FileBasedContentDefinition();

    result.setAnalyzeMode(analyzeMode);
    result.setName(contentName);
    result.setVersion(contentVersion);

    for (File binaryPath : binaryPaths) {
      result.addRootPath(binaryPath, ResourceType.BINARY);
    }

    if (sourcePaths != null) {
      for (File sourcePath : sourcePaths) {
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
}
