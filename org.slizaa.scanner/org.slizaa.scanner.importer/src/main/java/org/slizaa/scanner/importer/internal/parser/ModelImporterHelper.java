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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slizaa.scanner.spi.content.IPathIdentifier;
import org.slizaa.scanner.spi.content.IResource;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ModelImporterHelper {
  
  /**
   * @param specifiedResources
   * @param storedResources
   * @return
   */
  public static Set<IResource> computeNewAndModifiedResources(Collection<IResource> specifiedResources,
      Map<IPathIdentifier, StoredResourceNode> storedResources) {

    //
    Set<IResource> result = new HashSet<IResource>();

    try {

      //
      for (IResource resource : specifiedResources) {

        // get the associated resource
        StoredResourceNode storeResourceNode = storedResources.get(resource);

        // add if resource has to be re-parsed
        if (hasToBeReparsed(resource, storeResourceNode)) {
          result.add(resource);
        } else {
          storedResources.remove(resource);
        }
      }

    } finally {
      //
    }

    // return the result
    return result;
  }

  /**
   * @param resource
   * @param resourceBean
   * @return
   */
  public static boolean hasToBeReparsed(IResource resource, StoredResourceNode resourceBean) {

    // resource has to be re-parsed if no resource was stored in the
    // database
    if (resourceBean == null) {
      return true;
    }

    //
    if (resourceBean.isErroneous()) {
      return true;
    }

    // TODO
    // if (resource.isAnalyzeReferences() != resourceBean
    // .isAnalyzeReferences()) {
    // return true;
    // }

    // check the time stamp
    return resource.getTimestamp() != resourceBean.getTimestamp();
  }
}
