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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.importer.spi.parser.IParser;
import org.slizaa.scanner.importer.spi.parser.IParser.ParserType;
import org.slizaa.scanner.importer.spi.parser.IProblem;
import org.slizaa.scanner.model.IModifiableNode;
import org.slizaa.scanner.model.resource.IResourceNode;
import org.slizaa.scanner.model.resource.ResourceType;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IResource;

public class ParseJob implements Callable<List<IProblem>> {

  /** - */
  private final Logger          logger = LoggerFactory.getLogger(ParseJob.class);

  /** - */
  private IProgressMonitor      _progressMonitor;

  /** - */
  private IContentDefinition    _content;

  /** - */
  private Collection<Directory> _directories;

  /** - */
  private IParser[]             _parser;

  /** - */
  private BatchInserterFacade   _batchInserter;

  /** - */
  private boolean               _canceled;

  /** - */
  private IModifiableNode       _moduleNode;

  /**
   * <p>
   * Creates a new instance of type {@link ParserCallable}.
   * </p>
   * 
   * @param content
   * @param resources
   * @param batchInserter
   * @param resourceCache
   */
  public ParseJob(IContentDefinition content, IModifiableNode moduleNode, Collection<Directory> directories,
      IParser[] parser, BatchInserterFacade batchInserter, IProgressMonitor progressMonitor) {

    //
    checkNotNull(content);
    checkNotNull(directories);
    checkNotNull(parser);
    checkNotNull(batchInserter);

    //
    _content = content;
    _moduleNode = moduleNode;
    _directories = directories;
    _parser = parser;
    _progressMonitor = progressMonitor;
    _batchInserter = batchInserter;
  }

  /**
   * @return
   */
  public boolean isCanceled() {
    return _canceled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IProblem> call() throws Exception {

    logger.debug("Starting parse job...");

    //
    List<IProblem> problems = new LinkedList<IProblem>();

    try {
      //
      for (final Directory directory : _directories) {

        //
        if (checkIfCanceled(_progressMonitor)) {
          _canceled = true;
          return problems;
        }

        //
        if (!directory.getSourceResources().isEmpty()) {
          problems
              .addAll(parse(_content, directory.getSourceResources(), ResourceType.SOURCE, _parser, _progressMonitor));
        }
      }

      for (Directory directory : _directories) {

        //
        logger.debug("Parsing directory {}...", directory.getPath());

        //
        if (checkIfCanceled(_progressMonitor)) {
          _canceled = true;
          return problems;
        }

        //
        if (!directory.getBinaryResources().isEmpty()) {
          problems
              .addAll(parse(_content, directory.getBinaryResources(), ResourceType.BINARY, _parser, _progressMonitor));
        }
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    //
    return problems;
  }

  /**
   * @param content
   * @param resources
   * @param resourceType
   * @param parsers
   * @param monitor
   * @return
   */
  private List<IProblem> parse(IContentDefinition content, Collection<IResource> resources, ResourceType resourceType,
      IParser[] parsers, IProgressMonitor monitor) {

    //
    List<IProblem> result = new LinkedList<IProblem>();

    //
    for (final IResource resource : resources) {

      // create a resource node for the resource
      IModifiableNode resourceNode = _batchInserter.getOrCreateResourceNode(_moduleNode, resource, resourceType);

      for (IParser parser : parsers) {
        if (matches(parser, resourceType)) {

          //
          if (parser.canParse(resource)) {

            List<IProblem> problems = parser.parseResource(content, resource, resourceNode, new ParserContext(true));
            resourceNode.putProperty(IResourceNode.PROPERTY_ERRONEOUS, problems != null && !problems.isEmpty());
          }
        }
      }

      // resource has been processed
      if (monitor != null) {
        monitor.worked(1);
      }
    }

    //
    return result;
  }

  /**
   * <p>
   * Throws an {@link OperationCanceledException} if the underlying {@link IProgressMonitor} has been canceled.
   * </p>
   * 
   * @param monitor
   *          the monitor
   * @throws OperationCanceledException
   */
  static boolean checkIfCanceled(IProgressMonitor monitor) {
    return monitor != null && monitor.isCanceled();
  }

  /**
   * <p>
   * </p>
   * 
   * @param parser
   * @param resourceType
   * @return
   */
  private boolean matches(IParser parser, ResourceType resourceType) {

    //
    return parser.getParserType().equals(ParserType.BINARY_AND_SOURCE)
        || (ResourceType.SOURCE.equals(resourceType) && parser.getParserType().equals(ParserType.SOURCE))
        || (ResourceType.BINARY.equals(resourceType) && parser.getParserType().equals(ParserType.BINARY));
  }
}
