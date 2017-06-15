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

import java.util.List;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.importer.spi.content.IContentDefinition;
import org.slizaa.scanner.importer.spi.content.IContentDefinitions;

/**
 * <p>
 * Represents the definition of the system that should be analyzed. A {@link ISystemDefinition} can be created using the
 * {@link ISystemDefinitionFactory}.
 * </p>
 * <p>
 * 
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ISystemDefinition extends IContentDefinitions {

  /**
   * <p>
   * Returns an <b>unmodifiable</b> list of all registered {@link IContentDefinitionProvider
   * IContentDefinitionProviders}.
   * </p>
   * 
   * @return
   */
  List<? extends IContentDefinitionProvider> getContentDefinitionProviders();

  /**
   * <p>
   * Adds the specified {@link IContentDefinitionProvider} to this {@link ISystemDefinition}.
   * </p>
   * 
   * @param contentDefinitionProvider
   *          the {@link IContentDefinitionProvider} that should be added to this {@link ISystemDefinition}.
   */
  void addContentDefinitionProvider(IContentDefinitionProvider... contentDefinitionProvider);

  /**
   * <p>
   * Removes the specified {@link IContentDefinitionProvider} from this {@link ISystemDefinition}.
   * </p>
   * 
   * @param contentDefinitionProvider
   *          the {@link IContentDefinitionProvider} that should be removed from this {@link ISystemDefinition}.
   */
  void removeContentDefinitionProvider(IContentDefinitionProvider... contentDefinitionProvider);

  /**
   * <p>
   * Removes the {@link IContentDefinitionProvider IContentDefinitionProviders} with the specified identifier.
   * </p>
   * 
   * @param identifier
   *          the identifier of the {@link IContentDefinitionProvider IContentDefinitionProviders}.
   */
  void removeContentDefinitionProvider(String... identifier);

  /**
   * <p>
   * Moves the specified {@link IContentDefinitionProvider IContentDefinitionProviders} up one position.
   * </p>
   * 
   * @param contentDefinitionProviders
   *          the {@link IContentDefinitionProvider IContentDefinitionProviders} to move up.
   */
  void moveUpContentDefinitionProvider(IContentDefinitionProvider... contentDefinitionProviders);

  /**
   * <p>
   * Moves the specified {@link IContentDefinitionProvider IContentDefinitionProviders} down one position.
   * </p>
   * 
   * @param contentDefinitionProviders
   *          the {@link IContentDefinitionProvider IContentDefinitionProviders} to move up.
   */
  void moveDownContentDefinitionProvider(IContentDefinitionProvider... contentDefinitionProviders);

  /**
   * <p>
   * Clears the list of {@link IContentDefinitionProvider IContentDefinitionProviders}. After calling this method the
   * list of content definition providers is empty.
   * </p>
   */
  void clearContentDefinitionProviders();

  /**
   * <p>
   * Adds the specified {@link ISystemDefinitionChangedListener} to this {@link ISystemDefinition}.
   * </p>
   * 
   * @param systemChangedListener
   */
  void addSystemChangedListener(ISystemDefinitionChangedListener systemChangedListener);

  /**
   * <p>
   * Removes the specified {@link ISystemDefinitionChangedListener} from this {@link ISystemDefinition}.
   * </p>
   * 
   * @param systemChangedListener
   */
  void removeSystemChangedListener(ISystemDefinitionChangedListener systemChangedListener);

  /**
   * <p>
   * </p>
   *
   * @param changedListener
   */
  void addResourceChangedListener(IResourceChangedListener changedListener);

  /**
   * <p>
   * </p>
   *
   * @param changedListener
   */
  void removeResourceChangedListener(IResourceChangedListener changedListener);

  /**
   * <p>
   * Initializes this {@link ISystemDefinition}. Once the {@link ISystemDefinition} is initialized, the list of
   * {@link IContentDefinition IContentDefinitions} is resolved and can be read.
   * </p>
   * 
   * @param progressMonitor
   *          the {@link IProgressMonitor}, may <code>null</code>.
   */
  void initialize(IProgressMonitor progressMonitor);

  /**
   * <p>
   * Returns <code>true</code>, if this {@link ISystemDefinition} already has been initialized, <code>false</code>
   * otherwise.
   * </p>
   * 
   * @return
   */
  boolean isInitialized();

  /**
   * <p>
   * Returns a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
   * </p>
   * <p>
   * This method throws an {@link AssertionFailedException} if the {@link ISystemDefinition} is not initialized.
   * </p>
   * 
   * @return a <b>unmodifiable</b> list with all the defined {@link IContentDefinition IContentDefinitions}.
   */
  List<IContentDefinition> getContentDefinitions();
}
