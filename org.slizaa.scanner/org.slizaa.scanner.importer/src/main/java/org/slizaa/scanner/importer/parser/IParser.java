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
package org.slizaa.scanner.importer.parser;

import java.util.List;

import org.slizaa.scanner.importer.content.IContentDefinition;
import org.slizaa.scanner.importer.content.IResource;
import org.slizaa.scanner.model.IModifiableNode;

/**
 * <p>
 * Defines the common interface to parse a {@link IParsableResource}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IParser {

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public ParserType getParserType();

  /**
   * <p>
   * </p>
   * 
   * @param resource
   * @return
   */
  boolean canParse(IResource resource);

  /**
   * <p>
   * </p>
   * 
   * @param contentDefinition
   *          the content definition that specifies this resource.
   * @param resource
   *          the resource to parse
   * @param resourceBean
   *          the resource bean that represents the resource that has to be parsed
   * @param parseReferences
   *          whether or not to parse references
   * @param isBatchParse
   *          whether or not this method is called within a batch parse
   * @return
   */
  List<IProblem> parseResource(IContentDefinition contentDefinition, IResource resource, IModifiableNode resourceBean,
      boolean parseReferences, boolean isBatchParse);

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public enum ParserType {
    BINARY, BINARY_AND_SOURCE, SOURCE;
  }
}
