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
package org.slizaa.scanner.jtype.model.bytecode;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.slizaa.scanner.importer.content.ISystemDefinition;
import org.slizaa.scanner.importer.parser.IParser;
import org.slizaa.scanner.importer.parser.IParserFactory;
import org.slizaa.scanner.jtype.model.internal.Linker;
import org.slizaa.scanner.jtype.model.internal.bytecode.PostProcessor;
import org.slizaa.scanner.jtype.model.internal.primitvedatatypes.PrimitiveDatatypeNodeProvider;

/**
 * <p>
 * The {@link IParserFactory} to create instances of {@link JTypeByteCodeParser}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JTypeByteCodeParserFactory extends IParserFactory.Adapter implements IParserFactory {

  /** - */
  private Map<ISystemDefinition, PrimitiveDatatypeNodeProvider> _datatypeNodeProviderMap;

  /**
   * <p>
   * Creates a new instance of type {@link JTypeByteCodeParserFactory}.
   * </p>
   */
  public JTypeByteCodeParserFactory() {

    //
    _datatypeNodeProviderMap = new HashMap<ISystemDefinition, PrimitiveDatatypeNodeProvider>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IParser createParser(ISystemDefinition systemDefinition) {
    return new JTypeByteCodeParser(this, _datatypeNodeProviderMap.get(systemDefinition));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStart(ISystemDefinition systemDefinition, GraphDatabaseService graphDatabase, IProgressMonitor subMonitor) throws Exception {

    //
    synchronized (_datatypeNodeProviderMap) {
      if (!_datatypeNodeProviderMap.containsKey(systemDefinition)) {
        _datatypeNodeProviderMap.put(systemDefinition, new PrimitiveDatatypeNodeProvider(graphDatabase));
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void batchParseStop(ISystemDefinition systemDefinition, GraphDatabaseService graphDatabase, IProgressMonitor subMonitor) {

    // we have to create relationships between type references and types
    new Linker().link(graphDatabase, subMonitor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void beforeDeleteResourceNode(final Node node) {

    //
    PostProcessor.deleteAllJTypeRelatedNodesForResourceNode(node);
  }
}
