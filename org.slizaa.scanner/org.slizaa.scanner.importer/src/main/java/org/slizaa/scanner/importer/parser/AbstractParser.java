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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import org.slizaa.scanner.importer.content.IContentDefinition;
import org.slizaa.scanner.importer.content.IResource;
import org.slizaa.scanner.model.IModifiableNode;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public abstract class AbstractParser<P extends IParserFactory> implements IParser {

  /** - */
  private P              _parserFactory;

  /** - */
  private List<IProblem> _problems;

  /**
   * <p>
   * Creates a new instance of type {@link AbstractParser}.
   * </p>
   * 
   * @param parserFactory
   */
  public AbstractParser(P parserFactory) {

    //
    checkNotNull(parserFactory);

    //
    _parserFactory = parserFactory;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public P getParserFactory() {
    return _parserFactory;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected List<IProblem> getProblems() {
    return _problems;
  }

  /**
   * <p>
   * </p>
   * 
   * @param context
   * @param resourceKey
   */
  @Override
  public final List<IProblem> parseResource(IContentDefinition content, IResource resource,
      IModifiableNode resourceBean, boolean parseReferences, boolean isBatchParse) {

    // Reset problem list
    _problems = new LinkedList<IProblem>();

    // do the parsing
    doParseResource(content, resource, resourceBean, parseReferences, isBatchParse);

    //
    return _problems;
  }

  /**
   * Override in subclasses to implement parse logic
   * 
   * @param content
   * @param resource
   * @param context
   */
  protected abstract void doParseResource(IContentDefinition content, IResource resource, IModifiableNode resourceBean,
      boolean parseReferences, boolean isBatchParse);

  /**
   * <p>
   * </p>
   * 
   * @param resource
   * @return
   */
  public abstract boolean canParse(IResource resource);

}
