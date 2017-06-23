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

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.model.IModifiableNode;
import org.slizaa.scanner.model.INode;
import org.slizaa.scanner.model.NodeFactory;
import org.slizaa.scanner.model.resource.CoreModelElementType;
import org.slizaa.scanner.model.resource.CoreModelRelationshipType;
import org.slizaa.scanner.model.resource.IModuleNode;
import org.slizaa.scanner.spi.content.IContentDefinitions;
import org.slizaa.scanner.spi.content.IPathIdentifier;
import org.slizaa.scanner.spi.parser.IParserFactory;

import com.google.common.base.Stopwatch;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GraphDatabaseServiceFacade implements AutoCloseable {

  /** - */
  final Logger                 logger          = LoggerFactory.getLogger(GraphDatabaseServiceFacade.class);

  /** - */
  private GraphDatabaseService _graphDb;

  /** - */
  private IParserFactory[]     _parserFactories;

  /** - */
  private long                  _resourcesCount = -1;

  /**
   * <p>
   * Creates a new instance of type {@link GraphDatabaseServiceFacade}.
   * </p>
   * 
   * @param path
   */
  public GraphDatabaseServiceFacade(String path, IParserFactory[] parserFactories) {

    //
    checkNotNull(path);

    //
    _graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(new File(path)).newGraphDatabase();
    _parserFactories = parserFactories;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public long getStoredResourceCount() {

    //
    if (_resourcesCount == -1) {
      try (Transaction transaction = _graphDb.beginTx()) {

        //
        Result result = _graphDb
            .execute(String.format("MATCH (r:%s) RETURN count(r) as count", CoreModelElementType.RESOURCE.name()));

        //
        _resourcesCount = (long) result.columnAs("count").next();

        // commit
        transaction.success();
      }
    }

    //
    return _resourcesCount;
  }

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   */
  public void batchParseStart(IContentDefinitions systemDefinition, IProgressMonitor monitor) {

    //
    checkNotNull(systemDefinition);

    //
    for (IParserFactory parserFactory : _parserFactories) {

      //
      try (Transaction transaction = _graphDb.beginTx()) {

        //
        parserFactory.batchParseStart(systemDefinition, _graphDb, null);

        //
        transaction.success();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   */
  public void batchParseStop(IContentDefinitions systemDefinition, IProgressMonitor monitor) {

    //
    checkNotNull(systemDefinition);

    monitor = monitor == null ? new NullProgressMonitor() : monitor;

    // create the sub-monitor
    final SubMonitor subMonitor = SubMonitor.convert(monitor, _parserFactories.length);

    //
    for (IParserFactory parserFactory : _parserFactories) {

      //
      try (Transaction transaction = _graphDb.beginTx()) {

        //
        parserFactory.batchParseStop(systemDefinition, _graphDb, subMonitor);

        //
        transaction.success();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param progressMonitor
   * 
   * @return
   */
  public Map<IPathIdentifier, StoredResourceNode> readStoredResourceNodes(IProgressMonitor progressMonitor) {

    //
    logger.debug("entered readStoredResourceNodes()");

    //
    Map<IPathIdentifier, StoredResourceNode> map = new HashMap<IPathIdentifier, StoredResourceNode>();

    try (Transaction tx = _graphDb.beginTx()) {

      Stopwatch stopwatch = Stopwatch.createStarted();

      //
      logger.debug("Resource count {} ({}ms)", getStoredResourceCount(), stopwatch.elapsed(TimeUnit.MILLISECONDS));

      //
      if (progressMonitor != null) {
        progressMonitor.beginTask("Read stored resources...", (int)getStoredResourceCount());
      }

      //
      _graphDb.findNodes(CoreModelElementType.RESOURCE).forEachRemaining(storeResource -> {

        //
        Relationship relationship = storeResource.getSingleRelationship(CoreModelRelationshipType.CONTAINS,
            Direction.INCOMING);

        if (relationship == null) {
          System.out.println("HAE: " + storeResource);
        }

        //
        else {
          Node moduleNode = relationship.getStartNode();

          StoredResourceNode node = new StoredResourceNode(storeResource,
              (String) moduleNode.getProperty(IModuleNode.PROPERTY_CONTENT_ENTRY_ID));
          map.put(node, node);

        }
        //
        if (progressMonitor != null) {
          progressMonitor.worked(1);
        }

      });

    } finally {

      //
      logger.debug("readStoredResourceNodes() done.");

      //
      if (progressMonitor != null) {
        progressMonitor.done();
      }
    }

    // return
    return map;
  }

  /**
   * <p>
   * </p>
   *
   * @param progressMonitor
   * @return
   */
  public Map<String, IModifiableNode> readStoredModulesNodes(IProgressMonitor progressMonitor) {

    //
    Map<String, IModifiableNode> map = new HashMap<String, IModifiableNode>();

    Transaction tx = _graphDb.beginTx();

    try {

      //
      Result result = _graphDb
          .execute(String.format("MATCH (r:%s) RETURN count(r) as count", CoreModelElementType.MODULE.name()));
      long moduleCount = (long) result.columnAs("count").next();

      //
      if (progressMonitor != null) {
        progressMonitor.beginTask("Read stored modules...", (int)moduleCount);
      }

      //
      _graphDb.findNodes(CoreModelElementType.MODULE).stream().forEach(storedModule -> {

        //
        IModifiableNode moduleNodeBean = NodeFactory.createNode(storedModule.getId());
        moduleNodeBean.addLabel(CoreModelElementType.MODULE);
        moduleNodeBean.putProperty(IModuleNode.PROPERTY_MODULE_NAME,
            storedModule.getProperty(IModuleNode.PROPERTY_MODULE_NAME));
        moduleNodeBean.putProperty(IModuleNode.PROPERTY_MODULE_VERSION,
            storedModule.getProperty(IModuleNode.PROPERTY_MODULE_VERSION));
        moduleNodeBean.putProperty(IModuleNode.PROPERTY_CONTENT_ENTRY_ID,
            storedModule.getProperty(IModuleNode.PROPERTY_CONTENT_ENTRY_ID));

        map.put((String) moduleNodeBean.getProperties().get(IModuleNode.PROPERTY_CONTENT_ENTRY_ID), moduleNodeBean);

        //
        if (progressMonitor != null) {
          progressMonitor.worked(1);
        }
      });

    } finally {

      //
      tx.close();

      //
      if (progressMonitor != null) {
        progressMonitor.done();
      }
    }

    // return
    return map;
  }

  /**
   * <p>
   * </p>
   *
   * @param storedResourceNodes
   * @param monitor
   */
  public void deleteResourceNodes(Collection<StoredResourceNode> storedResourceNodes, IProgressMonitor monitor) {

    //
    if (monitor != null) {
      monitor.beginTask("Parsing...", storedResourceNodes.size());
    }

    //
    Transaction transaction = _graphDb.beginTx();

    //
    try {

      for (StoredResourceNode resourceNode : storedResourceNodes) {

        //
        Node node = _graphDb.getNodeById(resourceNode.getId());
        ParserFactoryAccess.onDeleteResourceNode(_parserFactories, node);

        //
        if (monitor != null) {
          monitor.worked(1);
        }
      }

    } finally {

      //
      try {
        transaction.success();
        transaction.close();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      //
      if (monitor != null) {
        monitor.done();
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param values
   * @param subMonitor
   */
  public void deleteModulesNodes(Collection<IModifiableNode> moduleNodes, IProgressMonitor monitor) {

    //
    if (monitor != null) {
      monitor.beginTask("Parsing...", moduleNodes.size());
    }

    //
    Transaction transaction = _graphDb.beginTx();

    //
    try {

      for (INode nodeBean : moduleNodes) {

        //
        Node node = _graphDb.getNodeById(nodeBean.getId());
        ParserFactoryAccess.onDeleteResourceNode(_parserFactories, node);

        //
        if (monitor != null) {
          monitor.worked(1);
        }
      }

    } finally {

      //
      transaction.success();
      transaction.close();

      //
      if (monitor != null) {
        monitor.done();
      }
    }

  }

  /**
   * <p>
   * </p>
   */
  public void close() {

    //
    _graphDb.shutdown();
  }
}
