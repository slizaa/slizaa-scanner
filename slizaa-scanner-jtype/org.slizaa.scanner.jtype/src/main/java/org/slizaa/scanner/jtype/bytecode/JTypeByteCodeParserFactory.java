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

import org.eclipse.core.runtime.IProgressMonitor;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.parser.IParser;
import org.slizaa.scanner.core.spi.parser.IParserFactory;
import org.slizaa.scanner.jtype.bytecode.internal.PrimitiveDatatypeNodeProvider;

/**
 * <p>
 * The {@link IParserFactory} to create instances of {@link JTypeByteCodeParser}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@SlizaaParserFactory
public class JTypeByteCodeParserFactory extends IParserFactory.Adapter implements IParserFactory {

  /** - */
  IPrimitiveDatatypeNodeProvider _datatypeNodeProvider = null;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public IPrimitiveDatatypeNodeProvider getDatatypeNodeProviderMap() {
    return _datatypeNodeProvider;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IParser createParser(IContentDefinitionProvider contentDefinition) {
    return new JTypeByteCodeParser(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStart(IContentDefinitionProvider contentDefinitions, Object graphDatabase,
      IProgressMonitor subMonitor) throws Exception {

    GraphDatabaseService graphDatabaseService = (GraphDatabaseService) graphDatabase;

    try (Transaction transaction = graphDatabaseService.beginTx()) {
      _datatypeNodeProvider = new PrimitiveDatatypeNodeProvider((GraphDatabaseService) graphDatabase);
    }

    //
    try (Transaction transaction = graphDatabaseService.beginTx()) {
      ((GraphDatabaseService) graphDatabase).execute("create index on :TYPE(fqn)");
      ((GraphDatabaseService) graphDatabase).execute("create index on :TYPE_REFERENCE(fqn)");
      ((GraphDatabaseService) graphDatabase).execute("create index on :METHOD(fqn)");
      ((GraphDatabaseService) graphDatabase).execute("create index on :METHOD_REFERENCE(fqn)");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStop(IContentDefinitionProvider contentDefinition, Object graphDatabase,
      IProgressMonitor subMonitor) {

    //
    _datatypeNodeProvider = null;
  }
}
