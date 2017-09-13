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

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.systemdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.IResourceChangedListener;
import org.slizaa.scanner.systemdefinition.ISystemDefinitionChangedListener;
import org.slizaa.scanner.systemdefinition.ISystemDefinitionWithWorkingCopy;
import org.slizaa.scanner.systemdefinition.SystemDefinitionChangedEvent;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SystemDefinitionWithWorkingCopy implements ISystemDefinitionWithWorkingCopy {

  /** - */
  private SystemDefinition _original;

  /** - */
  private SystemDefinition _backup;

  /**
   * <p>
   * Creates a new instance of type {@link SystemDefinitionWithWorkingCopy}.
   * </p>
   * 
   * @param original
   */
  public SystemDefinitionWithWorkingCopy(SystemDefinition original) {
    checkNotNull(original);

    //
    _original = original;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public SystemDefinition getOriginal() {
    assertNoWorkingCopy();
    return _original;
  }

  @Override
  public void addSystemChangedListener(ISystemDefinitionChangedListener systemChangedListener) {
    _original.addSystemChangedListener(systemChangedListener);
  }

  @Override
  public void removeSystemChangedListener(ISystemDefinitionChangedListener systemChangedListener) {
    _original.removeSystemChangedListener(systemChangedListener);
  }

  @Override
  public void addResourceChangedListener(IResourceChangedListener changedListener) {
    _original.addResourceChangedListener(changedListener);
  }

  @Override
  public void removeResourceChangedListener(IResourceChangedListener changedListener) {
    _original.removeResourceChangedListener(changedListener);
  }

  @Override
  public void startWorkingCopy() {
    assertNoWorkingCopy();

    // create working copy
    _backup = new SystemDefinition(_original);
  }

  @Override
  public void discardWorkingCopy() {
    assertWorkingCopy();

    if (!_original.equals(_backup)) {
      _original.mergeValues(_backup);
    }
    _backup = null;
  }

  @Override
  public void commitWorkingCopy() {
    assertWorkingCopy();

    _backup = null;
    
    _original.fireSystemDefinitionChangedEvent(new SystemDefinitionChangedEvent());
  }

  @Override
  public boolean isWorkingCopy() {
    return _backup != null;
  }

  @Override
  public boolean hasWorkingCopyChanged() {
    return _backup != null && !_original.equals(_backup);
  }

  @Override
  public List<? extends IContentDefinitionProvider> getContentDefinitionProviders() {
    return _original.getContentDefinitionProviders();
  }

  @Override
  public void addContentDefinitionProvider(IContentDefinitionProvider... contentDefinitionProvider) {
    _original.addContentDefinitionProvider(contentDefinitionProvider);
  }

  @Override
  public void removeContentDefinitionProvider(IContentDefinitionProvider... contentDefinitionProvider) {
    _original.removeContentDefinitionProvider(contentDefinitionProvider);
  }

  @Override
  public void removeContentDefinitionProvider(String... identifier) {
    _original.removeContentDefinitionProvider(identifier);
  }

  @Override
  public void moveUpContentDefinitionProvider(IContentDefinitionProvider... contentDefinitionProviders) {
    _original.moveUpContentDefinitionProvider(contentDefinitionProviders);
  }

  @Override
  public void moveDownContentDefinitionProvider(IContentDefinitionProvider... contentDefinitionProviders) {
    _original.moveDownContentDefinitionProvider(contentDefinitionProviders);
  }

  @Override
  public void clearContentDefinitionProviders() {
    _original.clearContentDefinitionProviders();
  }

  @Override
  public void initialize(IProgressMonitor progressMonitor) {
    _original.initialize(progressMonitor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInitialized() {
    return _backup == null && _original.isInitialized();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IContentDefinition> getContentDefinitions() {
    assertNoWorkingCopy();

    return _original.getContentDefinitions();
  }

  /**
   * <p>
   * </p>
   */
  private void assertNoWorkingCopy() {
    checkState(_backup == null, "System definition is a working copy, you have to commit or discard it.");
  }

  private void assertWorkingCopy() {
    checkState(_backup != null, "System definition is a not working copy, you have to start a working copy first.");
  }
}
