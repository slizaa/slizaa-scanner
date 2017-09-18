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
package org.slizaa.scanner.api.importer;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.spi.parser.IProblem;

/**
 * <p>
 * {@link IModelImporter IModelImporters} can be used to parse a system defined by a system definition into the
 * underlying graph database.
 * </p>
 * <p>
 * To create {@link IModelImporter} instances you have to use the {@link IModelImporterFactory}:
 * <code><pre>
 * // get the factory
 * IModelImporterFactory modelImporterFactory = ...;
 * 
 * // create a new IModelImporter instance
 * modelImporterFactory.createModelImporter(systemDefinition, databaseDirectory, parserFactories);
 * </pre></code>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IModelImporter {

  /**
   * <p>
   * Parses the underlying {@link IContentDefinitions}.
   * </p>
   * 
   * @param monitor
   *          the progress monitor
   * @return the list with all problems that may occurred while parsing the system
   */
  List<IProblem> parse(IProgressMonitor monitor);
}