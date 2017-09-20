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
package org.slizaa.scanner.itest.jtype.complex;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.kernel.api.exceptions.KernelException;
import org.ops4j.pax.url.mvn.MavenResolver;
import org.ops4j.pax.url.mvn.MavenResolvers;
import org.slizaa.scanner.itest.jtype.AbstractJTypeParserTest;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

/**
  */
public class SimpleJTypeMapStructTest extends AbstractJTypeParserTest {

  /** - */
  private static final String MAPSTRUCT           = "mapstruct";

  /** - */
  private static final String MAPSTRUCT_PROCESSOR = "mapstruct-processor";

  /** - */
  private static final String MAPSTRUCT_VERSION   = "1.0.0.Beta1";

  /** - */
  private File                _mapStructClassesJar;

  /** - */
  private File                _mapStructSourcesZip;

  /** - */
  private File                _mapStructProcessorClassesJar;

  /** - */
  private File                _mapStructProcessorSourcesZip;

  @Before
  public void before() throws IOException {

    // create the resolver
    MavenResolver mavenResolver = MavenResolvers.createMavenResolver(null, null);

    //
    _mapStructClassesJar = mavenResolver.resolve("org.mapstruct", MAPSTRUCT, null, "jar", MAPSTRUCT_VERSION);
    _mapStructSourcesZip = mavenResolver.resolve("org.mapstruct", MAPSTRUCT, "sources", "jar", MAPSTRUCT_VERSION);
    _mapStructProcessorClassesJar = mavenResolver.resolve("org.mapstruct", MAPSTRUCT_PROCESSOR, null, "jar",
        MAPSTRUCT_VERSION);
    _mapStructProcessorSourcesZip = mavenResolver.resolve("org.mapstruct", MAPSTRUCT_PROCESSOR, "sources", "jar",
        MAPSTRUCT_VERSION);

    //
    super.before();
  }

  /**
   * <p>
   * </p>
   * 
   * @throws KernelException
   * @throws IOException
   */
  @Test
  public void test() throws KernelException, IOException {

    //
    // //
    // System.out.println("Done.\n");
    //
    // System.out.println("Press ENTER to quit.");
    // System.in.read();
  }

  @Override
  protected ISystemDefinition getSystemDefinition() {
    try {
      return getSystemDefinition_BinariesOnly();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param descr
   * @return
   * @throws IOException
   */
  public ISystemDefinition getSystemDefinition_BinariesOnly() throws IOException {

    //
    ISystemDefinition descr = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(MAPSTRUCT, MAPSTRUCT_VERSION,
        AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(_mapStructClassesJar, ResourceType.BINARY);
    descr.addContentDefinitionProvider(provider);

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider2 = new FileBasedContentDefinitionProvider(MAPSTRUCT_PROCESSOR,
        MAPSTRUCT_VERSION, AnalyzeMode.BINARIES_ONLY);
    provider2.addRootPath(_mapStructProcessorClassesJar, ResourceType.BINARY);
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
   * @throws IOException
   */
  public ISystemDefinition getSystemDefinition_BinariesOnlyWithAttachedSources() throws IOException {

    //
    ISystemDefinition descr = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(MAPSTRUCT, MAPSTRUCT_VERSION,
        AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(_mapStructClassesJar, ResourceType.BINARY);
    provider.addRootPath(_mapStructSourcesZip, ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider);

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider2 = new FileBasedContentDefinitionProvider(MAPSTRUCT_PROCESSOR,
        MAPSTRUCT_VERSION, AnalyzeMode.BINARIES_ONLY);
    provider2.addRootPath(_mapStructProcessorClassesJar, ResourceType.BINARY);
    provider2.addRootPath(_mapStructProcessorSourcesZip, ResourceType.SOURCE);
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
   * @throws IOException
   */
  public ISystemDefinition getSystemDefinition_BinariesAndSources() throws IOException {

    //
    ISystemDefinition descr = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(MAPSTRUCT, MAPSTRUCT_VERSION,
        AnalyzeMode.BINARIES_AND_SOURCES);
    provider.addRootPath(_mapStructClassesJar, ResourceType.BINARY);
    provider.addRootPath(_mapStructSourcesZip, ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider);

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider2 = new FileBasedContentDefinitionProvider(MAPSTRUCT_PROCESSOR,
        MAPSTRUCT_VERSION, AnalyzeMode.BINARIES_AND_SOURCES);
    provider2.addRootPath(_mapStructProcessorClassesJar, ResourceType.BINARY);
    provider2.addRootPath(_mapStructProcessorSourcesZip, ResourceType.SOURCE);
    descr.addContentDefinitionProvider(provider2);

    // initialize
    descr.initialize(null);

    //
    return descr;
  }

}
