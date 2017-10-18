/*******************************************************************************
 * Copyright (C) 2017 wuetherich
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.neo4j.importer;

import java.io.File;

import org.slizaa.scanner.core.api.importer.IModelImporter;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.parser.IParserFactory;
import org.slizaa.scanner.neo4j.importer.internal.parser.ModelImporter;

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
