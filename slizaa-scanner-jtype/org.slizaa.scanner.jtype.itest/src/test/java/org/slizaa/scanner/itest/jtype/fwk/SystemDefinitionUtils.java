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
package org.slizaa.scanner.itest.jtype.fwk;

import java.io.File;

import org.slizaa.scanner.core.itestfwk.aether.AetherUtils;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

public class SystemDefinitionUtils {

  /** - */
  private static final String MAPSTRUCT           = "mapstruct";

  /** - */
  private static final String MAPSTRUCT_PROCESSOR = "mapstruct-processor";

  /** - */
  private static final String MAPSTRUCT_VERSION   = "1.0.0.Beta1";

  /**
   * <p>
   * </p>
   * 
   * @param descr
   * @return
   */
  public static ISystemDefinition getSystemDefinition_BinariesOnly() {

    //
    ISystemDefinition descr = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(MAPSTRUCT, MAPSTRUCT_VERSION,
        AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(getMapStructClassesJar(), ResourceType.BINARY);
    descr.addContentDefinitionProvider(provider);

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider2 = new FileBasedContentDefinitionProvider(MAPSTRUCT_PROCESSOR,
        MAPSTRUCT_VERSION, AnalyzeMode.BINARIES_ONLY);
    provider2.addRootPath(getMapStructProcessorClassesJar(), ResourceType.BINARY);
    descr.addContentDefinitionProvider(provider2);

    // initialize
    descr.initialize(null);

    //
    return descr;
  }

  /**
   * <p>
   * </p>
   * 
   * @param descr
   * @return
   */
  public static ISystemDefinition getSystemDefinition_BinariesOnlyWithAttachedSources() {

    //
    ISystemDefinition descr = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(MAPSTRUCT, MAPSTRUCT_VERSION,
        AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(getMapStructClassesJar(), ResourceType.BINARY);
    provider.addRootPath(getMapStructSourcesZip(), ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider);

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider2 = new FileBasedContentDefinitionProvider(MAPSTRUCT_PROCESSOR,
        MAPSTRUCT_VERSION, AnalyzeMode.BINARIES_ONLY);
    provider2.addRootPath(getMapStructProcessorClassesJar(), ResourceType.BINARY);
    provider2.addRootPath(getMapStructProcessorSourcesZip(), ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider2);

    // initialize
    descr.initialize(null);

    //
    return descr;
  }

  /**
   * <p>
   * </p>
   * 
   * @param descr
   * @return
   */
  public static ISystemDefinition getSystemDefinition_BinariesAndSources() {

    //
    ISystemDefinition descr = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(MAPSTRUCT, MAPSTRUCT_VERSION,
        AnalyzeMode.BINARIES_AND_SOURCES);
    provider.addRootPath(getMapStructClassesJar(), ResourceType.BINARY);
    provider.addRootPath(getMapStructSourcesZip(), ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider);

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider2 = new FileBasedContentDefinitionProvider(MAPSTRUCT_PROCESSOR,
        MAPSTRUCT_VERSION, AnalyzeMode.BINARIES_AND_SOURCES);
    provider2.addRootPath(getMapStructProcessorClassesJar(), ResourceType.BINARY);
    provider2.addRootPath(getMapStructProcessorSourcesZip(), ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider2);

    // initialize
    descr.initialize(null);

    //
    return descr;
  }

  public static File getMapStructClassesJar() {
    return AetherUtils.resolve("org.mapstruct", MAPSTRUCT, MAPSTRUCT_VERSION, null, "jar");
  }

  public static File getMapStructSourcesZip() {
    return AetherUtils.resolve("org.mapstruct", MAPSTRUCT, MAPSTRUCT_VERSION, "sources", "jar");
  }

  public static File getMapStructProcessorClassesJar() {
    return AetherUtils.resolve("org.mapstruct", MAPSTRUCT_PROCESSOR, MAPSTRUCT_VERSION, null, "jar");
  }

  public static File getMapStructProcessorSourcesZip() {
    return AetherUtils.resolve("org.mapstruct", MAPSTRUCT_PROCESSOR, MAPSTRUCT_VERSION, "sources", "jar");
  }
}
