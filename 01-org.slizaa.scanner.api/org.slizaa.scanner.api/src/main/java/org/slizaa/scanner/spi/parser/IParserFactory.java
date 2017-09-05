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
package org.slizaa.scanner.spi.parser;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IContentDefinitions;

/**
 * <p>
 * A parser factory is responsible for creating project parser.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IParserFactory {

  /**
   * <p>
   * This method is called immediately after a {@link IParserFactory} has been created.
   * </p>
   */
  void initialize();

  /**
   * <p>
   * This method is called before a {@link IParserFactory} will be destroyed.
   * </p>
   */
  void dispose();

  /**
   * <p>
   * Creates a new instance of type {@link IParser}.
   * </p>
   * 
   * @return the newly created {@link IParser}
   * @throws CoreException
   */
  IParser createParser(IContentDefinitions systemDefinition);

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param subMonitor
   *          TODO
   * @throws Exception
   */
  // TODO
  void batchParseStart(IContentDefinitions systemDefinition, Object graphDatabase, IProgressMonitor subMonitor)
      throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param graphDatabase
   * @param subMonitor
   *          TODO
   * @throws Exception
   */
  // TODO
  void batchParseStop(IContentDefinitions systemDefinition, Object graphDatabase, IProgressMonitor subMonitor)
      throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   * @throws Exception
   */
  void batchParseStartContentDefinition(IContentDefinition contentDefinition) throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   * @throws Exception
   */
  void batchParseStopContentDefinition(IContentDefinition contentDefinition) throws Exception;

  /**
   * <p>
   * </p>
   * 
   * @param node
   */
  // TODO!
  void beforeDeleteResourceNode(Object node);

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  static abstract class Adapter implements IParserFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStart(IContentDefinitions systemDefinition, Object graphDatabase, IProgressMonitor subMonitor)
        throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStop(IContentDefinitions systemDefinition, Object graphDatabase, IProgressMonitor subMonitor)
        throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStartContentDefinition(IContentDefinition contentDefinition) throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void batchParseStopContentDefinition(IContentDefinition contentDefinition) throws Exception {
      // empty default implementation
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeDeleteResourceNode(Object node) {
      // empty default implementation
    }
  }
}
