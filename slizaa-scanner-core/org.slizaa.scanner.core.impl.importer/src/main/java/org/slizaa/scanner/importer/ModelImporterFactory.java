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
package org.slizaa.scanner.importer;

import java.io.File;

import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.importer.internal.parser.ModelImporter;
import org.slizaa.scanner.spi.content.IContentDefinitionProvider;
import org.slizaa.scanner.spi.parser.IParserFactory;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ModelImporterFactory implements IModelImporterFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public IModelImporter createModelImporter(IContentDefinitionProvider systemDefniition, File databaseDirectory,
      IParserFactory... parserFactories) {

    //
    return new ModelImporter(systemDefniition, databaseDirectory, parserFactories);
  }
}
