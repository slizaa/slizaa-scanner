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

import org.neo4j.graphdb.Node;
import org.slizaa.scanner.api.model.resource.IResourceNode;
import org.slizaa.scanner.api.model.resource.ResourceType;
import org.slizaa.scanner.importer.internal.LRCache;
import org.slizaa.scanner.spi.content.IPathIdentifier;

public class StoredResourceNode implements IPathIdentifier {

  private String       _contentDefinitionId;

  private String       _root;

  private String       _path;

  private long         _id;

  private long         _timestamp;

  private boolean      _isErroneous;

  private boolean      _isAnalyzeReferences;

  private ResourceType _resourceType;

  public StoredResourceNode(String contentDefinitionId, long id, String root, String path, ResourceType resourceType,
      long timestamp, boolean isErroneous, boolean isAnalyzeReferences) {

    checkNotNull(contentDefinitionId);
    checkNotNull(root);
    checkNotNull(path);
    checkNotNull(resourceType);

    //
    _contentDefinitionId = contentDefinitionId;

    //
    _id = id;
    _root = root;
    _path = path;
    _resourceType = resourceType;
    _timestamp = timestamp;
    _isErroneous = isErroneous;
    _isAnalyzeReferences = isAnalyzeReferences;
  }

  /**
   * <p>
   * Creates a new instance of type {@link StoredResourceNode}.
   * </p>
   * 
   * @param node
   * @param contentDefinitionId
   */
  public StoredResourceNode(Node node, String contentDefinitionId) {

    _root = (String) node.getProperty(IResourceNode.PROPERTY_ROOT);
    _path = (String) node.getProperty(IResourceNode.PROPERTY_PATH);
    _id = node.getId();
    _timestamp = (long) node.getProperty(IResourceNode.PROPERTY_TIMESTAMP);
    _isErroneous = (boolean) node.getProperty(IResourceNode.PROPERTY_ERRONEOUS);
    _isAnalyzeReferences = (boolean) node.getProperty(IResourceNode.PROPERTY_ANALYSE_REFERENCES);

    //
    if (node.hasLabel(LRCache.convert(ResourceType.SOURCE))) {
      _resourceType = ResourceType.SOURCE;
    } else if (node.hasLabel(LRCache.convert(ResourceType.BINARY))) {
      _resourceType = ResourceType.BINARY;
    }

    //
    _contentDefinitionId = contentDefinitionId;
  }

  public String getContentDefinitionId() {
    return _contentDefinitionId;
  }

  @Override
  public String getRoot() {
    return _root;
  }

  @Override
  public String getPath() {
    return _path;
  }

  public long getId() {
    return _id;
  }

  public long getTimestamp() {
    return _timestamp;
  }

  public boolean isErroneous() {
    return _isErroneous;
  }

  public ResourceType getResourceType() {
    return _resourceType;
  }

  public boolean isAnalyzeReferences() {
    return _isAnalyzeReferences;
  }

  public int hashCode() {
    return IPathIdentifier.Helper.hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return IPathIdentifier.Helper.equals(this, obj);
  }
}
