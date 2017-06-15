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
package org.slizaa.scanner.importer.internal.parser;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.slizaa.scanner.importer.spi.parser.IParserFactory;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ParserFactoryAccess {

  /**
   * <p>
   * </p>
   * 
   * @param parserFactories
   * @param node
   */
  public static void onDeleteResourceNode(IParserFactory[] parserFactories, Node node) {

    //
    for (IParserFactory parserFactory : parserFactories) {
      parserFactory.beforeDeleteResourceNode(node);
    }

    for (Relationship relationship : node.getRelationships()) {
      relationship.delete();
    }
    node.delete();

    //
    // TODO: delegate to ParserFactory
  }
}
