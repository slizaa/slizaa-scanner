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
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.slizaa.scanner.importer.content.IContentDefinition;
import org.slizaa.scanner.importer.content.IResource;
import org.slizaa.scanner.importer.content.ISystemDefinition;
import org.slizaa.scanner.model.IModifiableNode;
import org.slizaa.scanner.model.INode;
import org.slizaa.scanner.model.IRelationship;
import org.slizaa.scanner.model.NodeFactory;
import org.slizaa.scanner.model.resource.CoreModelElementType;
import org.slizaa.scanner.model.resource.CoreModelRelationshipType;
import org.slizaa.scanner.model.resource.IModuleNode;
import org.slizaa.scanner.model.resource.IResourceNode;
import org.slizaa.scanner.model.resource.ResourceType;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class BatchInserterFacade implements AutoCloseable {

  /** - */
  private BatchInserter                   _batchInserter;

  /** - */
  private String                          _storeDir;

  /** - */
  private Map<IResource, IModifiableNode> _resourcesMap;

  /** - */
  private Map<String, INode>              _modulesMap;

  /**
   * <p>
   * Creates a new instance of type {@link BatchInserterFacade}.
   * </p>
   * 
   * @param storeDir
   */
  public BatchInserterFacade(String storeDir) {

    //
    checkNotNull(storeDir);
    _storeDir = storeDir;

    // TODO!
    try {
      _batchInserter = BatchInserters.inserter(new File(_storeDir));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    //
    _resourcesMap = new HashMap<>();
    _modulesMap = new HashMap<>();
  }

  /**
   * <p>
   * </p>
   * 
   * @param batchInserter
   * @param contentDefinition
   * @return
   */
  public IModifiableNode getOrCreateModuleNode(final IContentDefinition contentDefinition) {

    //
    String contentDefinitionId = contentDefinition.getId();

    //
    if (!_modulesMap.containsKey(contentDefinitionId)) {

      //
      IModifiableNode moduleNode = NodeFactory.createNode();
      moduleNode.addLabel(CoreModelElementType.Module);
      moduleNode.putProperty(IModuleNode.PROPERTY_MODULE_NAME, contentDefinition.getName());
      moduleNode.putProperty(IModuleNode.PROPERTY_MODULE_VERSION, contentDefinition.getVersion());
      moduleNode.putProperty(IModuleNode.PROPERTY_CONTENT_ENTRY_ID, contentDefinitionId);
      _modulesMap.put(contentDefinitionId, moduleNode);
    }

    //
    return (IModifiableNode) _modulesMap.get(contentDefinitionId);
  }

  /**
   * <p>
   * </p>
   * 
   * @param resource
   * @return
   */
  public IModifiableNode getOrCreateResourceNode(final IModifiableNode parentModuleNode, final IResource resource,
      final ResourceType resourceType) {

    return getOrCreateResourceNode(resource, new INodeCreator() {

      @Override
      public IModifiableNode createBean() {
        IModifiableNode resourceNode = NodeFactory.createNode();
        resourceNode.addLabel(CoreModelElementType.Resource);
        resourceNode.addLabel(resourceType);
        resourceNode.putProperty(IResourceNode.PROPERTY_ROOT, resource.getRoot());
        resourceNode.putProperty(IResourceNode.PROPERTY_PATH, resource.getPath());
        resourceNode.putProperty(IResourceNode.PROPERTY_TIMESTAMP, resource.getTimestamp());
        resourceNode.putProperty(IResourceNode.PROPERTY_ERRONEOUS, false);
        resourceNode.putProperty(IResourceNode.PROPERTY_ANALYSE_REFERENCES, true);
        parentModuleNode.addRelationship(resourceNode, CoreModelRelationshipType.CONTAINS);
        return resourceNode;
      }
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Map<IResource, IModifiableNode> getResourcesMap() {
    return Collections.unmodifiableMap(_resourcesMap);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public Map<String, INode> getModulesMap() {
    return Collections.unmodifiableMap(_modulesMap);
  }

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param storedModulesMap
   */
  public void setupModuleNodes(ISystemDefinition systemDefinition, Map<String, IModifiableNode> storedModulesMap) {

    //
    for (IContentDefinition contentDefinition : systemDefinition.getContentDefinitions()) {
      if (storedModulesMap.containsKey(contentDefinition.getId())) {
        _modulesMap.put(contentDefinition.getId(), storedModulesMap.remove(contentDefinition.getId()));
      }
    }
  }

  public void clearResourceMap() {
    _resourcesMap.clear();
  }

  /**
   * <p>
   * </p>
   * 
   * @param nodeBean
   * @return
   */
  public long create(INode nodeBean) {

    //
    long id = -1;

    if (!nodeBean.hasNodeId()) {

      //
      id = _batchInserter.createNode(nodeBean.getProperties(), nodeBean.getLabels().toArray(new Label[0]));
      ((IModifiableNode) nodeBean).setNodeId(id);
    } else {
      id = nodeBean.getId();
    }

    //
    for (Map.Entry<RelationshipType, List<IRelationship>> entry : nodeBean.getRelationships().entrySet()) {
      for (IRelationship relationship : entry.getValue()) {
        long newId = create(relationship.getTargetBean());
        _batchInserter.createRelationship(id, newId, entry.getKey(), relationship.getRelationshipProperties());
      }
    }

    //
    return id;
  }

  /**
   * <p>
   * </p>
   * 
   * @param resourceId
   * @param nodeCreator
   * @return
   */
  private synchronized IModifiableNode getOrCreateResourceNode(IResource resourceId, INodeCreator nodeCreator) {

    //
    if (!_resourcesMap.containsKey(resourceId)) {
      _resourcesMap.put(resourceId, nodeCreator.createBean());
    }

    //
    return _resourcesMap.get(resourceId);
  }

  public synchronized void close() {
    _batchInserter.shutdown();
  }
}
