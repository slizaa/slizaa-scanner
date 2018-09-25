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
package org.slizaa.neo4j.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.spi.contentdefinition.IContentDefinition;
import org.slizaa.scanner.spi.contentdefinition.filebased.IFile;
import org.slizaa.scanner.spi.parser.IParser;
import org.slizaa.scanner.spi.parser.IProblem;
import org.slizaa.scanner.spi.parser.ParserType;
import org.slizaa.scanner.spi.parser.model.INode;
import org.slizaa.scanner.spi.parser.model.resource.IResourceNode;
import org.slizaa.scanner.spi.parser.model.resource.ResourceType;

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
  private INode                 _moduleNode;

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
  public ParseJob(IContentDefinition content, INode moduleNode, Collection<Directory> directories, IParser[] parser,
      BatchInserterFacade batchInserter, IProgressMonitor progressMonitor) {

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
          problems.addAll(parse(_content, _batchInserter.getDirectoriesMap().get(directory.getPath()),
              directory.getSourceResources(), ResourceType.Source, _parser, _progressMonitor));
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
          problems.addAll(parse(_content, _batchInserter.getOrCreateDirectoyNode(directory, _moduleNode),
              directory.getBinaryResources(), ResourceType.Binary, _parser, _progressMonitor));
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
  private List<IProblem> parse(IContentDefinition content, INode directoryParentNode, Collection<IFile> resources,
      ResourceType resourceType, IParser[] parsers, IProgressMonitor monitor) {

    //
    List<IProblem> result = new LinkedList<IProblem>();

    //
    for (final IFile resource : resources) {

      // create a resource node for the resource
      INode resourceNode = _batchInserter.getOrCreateResourceNode(_moduleNode, directoryParentNode, resource,
          resourceType);

      for (IParser parser : parsers) {
        if (matches(parser, resourceType)) {

          //
          if (parser.canParse(resource)) {

            List<IProblem> problems = parser.parseResource(content, resource, resourceNode,
                new ParserContext(_moduleNode, (INode) directoryParentNode, true));
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
        || (ResourceType.Source.equals(resourceType) && parser.getParserType().equals(ParserType.SOURCE))
        || (ResourceType.Binary.equals(resourceType) && parser.getParserType().equals(ParserType.BINARY));
  }
}
