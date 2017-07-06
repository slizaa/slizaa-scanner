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
import java.util.function.Supplier;

import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.api.model.IRelationship;
import org.slizaa.scanner.api.model.Label;
import org.slizaa.scanner.api.model.NodeFactory;
import org.slizaa.scanner.api.model.RelationshipType;
import org.slizaa.scanner.api.model.resource.CoreModelElementType;
import org.slizaa.scanner.api.model.resource.CoreModelRelationshipType;
import org.slizaa.scanner.api.model.resource.IDirectoryNode;
import org.slizaa.scanner.api.model.resource.IModuleNode;
import org.slizaa.scanner.api.model.resource.IResourceNode;
import org.slizaa.scanner.api.model.resource.ResourceType;
import org.slizaa.scanner.importer.internal.LabelAndRelationshipCache;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IContentDefinitions;
import org.slizaa.scanner.spi.content.IResource;

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
  private Map<String, IModifiableNode>    _directoriesMap;

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
    _directoriesMap = new HashMap<>();
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
      moduleNode.addLabel(CoreModelElementType.MODULE);
      moduleNode.putProperty(IModuleNode.PROPERTY_MODULE_NAME, contentDefinition.getName());
      moduleNode.putProperty(INode.FQN, contentDefinition.getName());
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
  public IModifiableNode getOrCreateResourceNode(final IModifiableNode parentModuleNode,
      final INode parentDirectoryNode, final IResource resource, final ResourceType resourceType) {

    //
    return getOrCreateResourceNode(resource, () -> {
      IModifiableNode resourceNode = NodeFactory.createNode();
      resourceNode.addLabel(CoreModelElementType.RESOURCE);
      resourceNode.addLabel(resourceType);
      resourceNode.putProperty(INode.NAME, resource.getName());
      resourceNode.putProperty(INode.FQN, resource.getPath());
      resourceNode.putProperty(IResourceNode.PROPERTY_PATH, resource.getPath());
      resourceNode.putProperty(IResourceNode.PROPERTY_ROOT, resource.getRoot());
      resourceNode.putProperty(IResourceNode.PROPERTY_PATH, resource.getPath());
      resourceNode.putProperty(IResourceNode.PROPERTY_TIMESTAMP, resource.getTimestamp());
      resourceNode.putProperty(IResourceNode.PROPERTY_ERRONEOUS, false);
      resourceNode.putProperty(IResourceNode.PROPERTY_ANALYSE_REFERENCES, true);
      parentModuleNode.addRelationship(resourceNode, CoreModelRelationshipType.CONTAINS);

      synchronized (parentDirectoryNode) {
        ((IModifiableNode) parentDirectoryNode).addRelationship(resourceNode, CoreModelRelationshipType.CONTAINS);
      }

      return resourceNode;
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
   * @return
   */
  public Map<String, INode> getDirectoriesMap() {
    return Collections.unmodifiableMap(_directoriesMap);
  }

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param storedModulesMap
   */
  public void setupModuleNodes(IContentDefinitions systemDefinition, Map<String, IModifiableNode> storedModulesMap) {

    //
    for (IContentDefinition contentDefinition : systemDefinition.getContentDefinitions()) {
      if (storedModulesMap.containsKey(contentDefinition.getId())) {
        _modulesMap.put(contentDefinition.getId(), storedModulesMap.remove(contentDefinition.getId()));
      }
    }
  }

  public void clearResourceAndDirectoriesMap() {
    _resourcesMap.clear();
    _directoriesMap.clear();
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
      id = _batchInserter.createNode(nodeBean.getProperties(),
          LabelAndRelationshipCache.convert(nodeBean.getLabels().toArray(new Label[0])));
      
      ((IModifiableNode) nodeBean).setNodeId(id);
      
      //
      for (Map.Entry<RelationshipType, List<IRelationship>> entry : nodeBean.getRelationships().entrySet()) {
        for (IRelationship relationship : entry.getValue()) {
          long newId = create(relationship.getTargetBean());
          _batchInserter.createRelationship(id, newId, LabelAndRelationshipCache.convert(entry.getKey()),
              relationship.getRelationshipProperties());
        }
      }
    } 
    
    return nodeBean.getId();
  }

  public IModifiableNode getOrCreateDirectoyNode(Directory directoryPath, IModifiableNode moduleNode) {

    //
    String[] pathElements = directoryPath.getPath().split("/");

    // create complete structure
    String currentPath = "";
    IModifiableNode hierarchicalParent = moduleNode;
    IModifiableNode currentDirectory = null;
    for (String element : pathElements) {
      currentPath = currentPath.isEmpty() ? element : currentPath + "/" + element;
      currentDirectory = _getOrCreateDirectoyNode(currentPath, hierarchicalParent, moduleNode);
      hierarchicalParent = currentDirectory;
    }

    //
    currentDirectory.putProperty(IDirectoryNode.PROPERTY_IS_EMPTY, false);

    //
    return currentDirectory;
  }

  /**
   * <p>
   * </p>
   *
   * ${tags}
   */
  private IModifiableNode _getOrCreateDirectoyNode(String path, IModifiableNode hierarchicalParent,
      IModifiableNode moduleNode) {

    //
    return _directoriesMap.computeIfAbsent(path, id -> {

      //
      IModifiableNode directoryNode = NodeFactory.createNode();
      directoryNode.addLabel(CoreModelElementType.DIRECTORY);
      directoryNode.putProperty(INode.FQN, path);
      String name = path.lastIndexOf('/') != -1 ? path.substring(path.lastIndexOf('/') + 1) : path;
      directoryNode.putProperty(INode.NAME, name);
      directoryNode.putProperty(IDirectoryNode.PROPERTY_PATH, path);
      directoryNode.putProperty(IDirectoryNode.PROPERTY_IS_EMPTY, true);

      //
      if (hierarchicalParent != null && !hierarchicalParent.equals(moduleNode)) {
        hierarchicalParent.addRelationship(directoryNode, CoreModelRelationshipType.CONTAINS);
      }
      moduleNode.addRelationship(directoryNode, CoreModelRelationshipType.CONTAINS);

      //
      return directoryNode;
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @param resourceId
   * @param nodeCreator
   * @return
   */
  private IModifiableNode getOrCreateResourceNode(IResource resourceId, Supplier<IModifiableNode> nodeCreator) {

    //
    IModifiableNode node = _resourcesMap.get(resourceId);
    if (node == null) {
      synchronized (this) {
        if (!_resourcesMap.containsKey(resourceId)) {
          node = nodeCreator.get();
          _resourcesMap.put(resourceId, node);
        }
      }
    }

    //
    return _resourcesMap.get(resourceId);
  }

  /**
   * {@inheritDoc}
   */
  public synchronized void close() {
    _batchInserter.shutdown();
  }
}
