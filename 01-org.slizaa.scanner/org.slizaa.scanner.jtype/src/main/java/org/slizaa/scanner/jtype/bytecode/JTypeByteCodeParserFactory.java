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
package org.slizaa.scanner.jtype.bytecode;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.slizaa.scanner.jtype.bytecode.internal.PostProcessor;
import org.slizaa.scanner.jtype.bytecode.internal.PrimitiveDatatypeNodeProvider;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IContentDefinitions;
import org.slizaa.scanner.spi.parser.IParser;
import org.slizaa.scanner.spi.parser.IParserFactory;

/**
 * <p>
 * The {@link IParserFactory} to create instances of {@link JTypeByteCodeParser}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JTypeByteCodeParserFactory extends IParserFactory.Adapter implements IParserFactory {

  /** - */
  Map<IContentDefinitions, IPrimitiveDatatypeNodeProvider> _datatypeNodeProviderMap;

  /**
   * <p>
   * Creates a new instance of type {@link JTypeByteCodeParserFactory}.
   * </p>
   */
  public JTypeByteCodeParserFactory() {

    //
    _datatypeNodeProviderMap = new HashMap<>();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public IPrimitiveDatatypeNodeProvider getDatatypeNodeProviderMap(IContentDefinition contentDefinition) {
    return _datatypeNodeProviderMap.get(checkNotNull(contentDefinition).getContentDefinitions());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IParser createParser(IContentDefinitions contentDefinition) {
    return new JTypeByteCodeParser(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStart(IContentDefinitions contentDefinitions, Object graphDatabase, IProgressMonitor subMonitor)
      throws Exception {

    //
    synchronized (_datatypeNodeProviderMap) {
      if (!_datatypeNodeProviderMap.containsKey(contentDefinitions)) {
        _datatypeNodeProviderMap.put(contentDefinitions,
            new PrimitiveDatatypeNodeProvider((GraphDatabaseService) graphDatabase));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStop(IContentDefinitions contentDefinition, Object graphDatabase, IProgressMonitor subMonitor) {

    //
    ((GraphDatabaseService) graphDatabase).execute("MATCH (n:DIRECTORY)-[:CONTAINS*]->(t:PACKAGE) set n :PACKAGE ");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beforeDeleteResourceNode(final Object node) {

    //
    PostProcessor.deleteAllJTypeRelatedNodesForResourceNode((Node) node);
  }
}
