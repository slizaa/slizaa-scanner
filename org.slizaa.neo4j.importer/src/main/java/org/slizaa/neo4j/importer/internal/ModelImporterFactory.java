/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
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
package org.slizaa.neo4j.importer.internal;

import java.io.File;
import java.util.List;

import org.slizaa.scanner.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.neo4j.importer.internal.parser.ModelImporter;

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
  public IModelImporter createModelImporter(IContentDefinitionProvider contentDefinitionProvider,
      File databaseDirectory, List<IParserFactory> parserFactories, List<ICypherStatement> cypherStatements) {

    //
    return new ModelImporter(contentDefinitionProvider, databaseDirectory, parserFactories, cypherStatements);
  }
}
