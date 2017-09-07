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
import org.eclipse.core.runtime.SubMonitor;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.slizaa.scanner.jtype.bytecode.internal.PostProcessor;
import org.slizaa.scanner.jtype.bytecode.internal.PrimitiveDatatypeNodeProvider;
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
  public IParser createParser(IContentDefinitions contentDefinition) {
    return new JTypeByteCodeParser(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStart(IContentDefinitions contentDefinitions, Object graphDatabase, IProgressMonitor subMonitor)
      throws Exception {

    GraphDatabaseService graphDatabaseService = (GraphDatabaseService) graphDatabase;

    try (Transaction transaction = graphDatabaseService.beginTx()) {
      _datatypeNodeProvider = new PrimitiveDatatypeNodeProvider((GraphDatabaseService) graphDatabase);
    }

    //
    try (Transaction transaction = graphDatabaseService.beginTx()) {
      ((GraphDatabaseService) graphDatabase).execute("create index on :TYPE(fqn)");
      ((GraphDatabaseService) graphDatabase).execute("create index on :TYPE_REFERENCE(fqn)");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStop(IContentDefinitions contentDefinition, Object graphDatabase, IProgressMonitor subMonitor) {

    //
    _datatypeNodeProvider = null;

    //
    GraphDatabaseService graphDatabaseService = (GraphDatabaseService) graphDatabase;

    //
    final SubMonitor progressMonitor = SubMonitor.convert(subMonitor, 2);

    // TODO
    // MATCH (p:PACKAGE)-[:CONTAINS]->(r:RESOURCE)-[:CONTAINS]->(t:TYPE) RETURN t
    // MATCH (m:MODULE)-[:CONTAINS]->(p:PACKAGE)-[:CONTAINS]->(r:RESOURCE)-[:CONTAINS]->(t:TYPE) RETURN t
    
    progressMonitor.newChild(1);
    graphDatabaseService.execute("MATCH (n:DIRECTORY)-[:CONTAINS*]->(t:PACKAGE) set n :PACKAGE");

    progressMonitor.newChild(1);
    graphDatabaseService.execute(
        "MATCH (t:TYPE) WITH t, t.fqn as tfqn MATCH (tref:TYPE_REFERENCE) WHERE tref.fqn = tfqn CREATE (tref)-[:BOUND_TO]->(t)");
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
