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
package org.slizaa.scanner.itest.framework;

import static org.slizaa.scanner.itest.framework.TestFrameworkUtils.getMapStructClassesJar;
import static org.slizaa.scanner.itest.framework.TestFrameworkUtils.getMapStructName;
import static org.slizaa.scanner.itest.framework.TestFrameworkUtils.getMapStructProcessorClassesJar;
import static org.slizaa.scanner.itest.framework.TestFrameworkUtils.getMapStructProcessorName;
import static org.slizaa.scanner.itest.framework.TestFrameworkUtils.getMapStructProcessorSourcesZip;
import static org.slizaa.scanner.itest.framework.TestFrameworkUtils.getMapStructSourcesZip;
import static org.slizaa.scanner.itest.framework.TestFrameworkUtils.getMapStructVersion;

import org.slizaa.scanner.importer.content.AnalyzeMode;
import org.slizaa.scanner.importer.content.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.importer.content.ISystemDefinition;
import org.slizaa.scanner.model.resource.ResourceType;

public class SystemDefinitionUtils {

  public static ISystemDefinition getSystemDefinition_BinariesOnly(ISystemDefinition descr) {

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(getMapStructName(),
        getMapStructVersion(), AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(getMapStructClassesJar(), ResourceType.BINARY);
    descr.addContentDefinitionProvider(provider);

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider2 = new FileBasedContentDefinitionProvider(getMapStructProcessorName(),
        getMapStructVersion(), AnalyzeMode.BINARIES_ONLY);
    provider2.addRootPath(getMapStructProcessorClassesJar(), ResourceType.BINARY);
    descr.addContentDefinitionProvider(provider2);

    // initialize
    descr.initialize(null);

    //
    return descr;
  }

  public static ISystemDefinition getSystemDefinition_BinariesOnlyWithAttachedSources(ISystemDefinition descr) {

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(getMapStructName(),
        getMapStructVersion(), AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(getMapStructClassesJar(), ResourceType.BINARY);
    provider.addRootPath(getMapStructSourcesZip(), ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider);

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider2 = new FileBasedContentDefinitionProvider(getMapStructProcessorName(),
        getMapStructVersion(), AnalyzeMode.BINARIES_ONLY);
    provider2.addRootPath(getMapStructProcessorClassesJar(), ResourceType.BINARY);
    provider2.addRootPath(getMapStructProcessorSourcesZip(), ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider2);

    // initialize
    descr.initialize(null);

    //
    return descr;
  }

  public static ISystemDefinition getSystemDefinition_BinariesAndSources(ISystemDefinition descr) {

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(getMapStructName(),
        getMapStructVersion(), AnalyzeMode.BINARIES_AND_SOURCES);
    provider.addRootPath(getMapStructClassesJar(), ResourceType.BINARY);
    provider.addRootPath(getMapStructSourcesZip(), ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider);

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider2 = new FileBasedContentDefinitionProvider(getMapStructProcessorName(),
        getMapStructVersion(), AnalyzeMode.BINARIES_AND_SOURCES);
    provider2.addRootPath(getMapStructProcessorClassesJar(), ResourceType.BINARY);
    provider2.addRootPath(getMapStructProcessorSourcesZip(), ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider2);

    // initialize
    descr.initialize(null);

    //
    return descr;
  }
}
