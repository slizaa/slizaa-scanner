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
package org.slizaa.scanner.systemdefinition.internal;

import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CustomFileBasedContentDefinitionProvider extends FileBasedContentDefinitionProvider {

  public CustomFileBasedContentDefinitionProvider() {
    super();
  }

  public CustomFileBasedContentDefinitionProvider(String name, String version, AnalyzeMode analyzeMode) {
    super(name, version, analyzeMode);
  }

  @Override
  public void handleResourceAdded(IContentDefinition contentEntry, String root, String path, ResourceType type) {
    super.handleResourceAdded(contentEntry, root, path, type);
  }
}
