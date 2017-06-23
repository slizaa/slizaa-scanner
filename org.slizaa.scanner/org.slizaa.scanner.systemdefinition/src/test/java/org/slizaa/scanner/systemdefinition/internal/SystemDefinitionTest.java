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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slizaa.scanner.model.resource.ResourceType;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

public class SystemDefinitionTest {

  /** - */
  private File              _targetFile;

  private ISystemDefinition _systemDefinition;

  /**
   * <p>
   * </p>
   */
  @Before
  public void setup() {

    _targetFile = new File(
        String.format("%s/target/junit/test_%s.json", System.getProperty("user.dir"), System.currentTimeMillis()));
    _targetFile.getParentFile().mkdirs();

    _systemDefinition = new SystemDefinitionFactory().createNewSystemDefinition();

    FileBasedContentDefinitionProvider basedContentDefinitionProvider = new FileBasedContentDefinitionProvider(
        "TestName", "1.2.3.4", AnalyzeMode.BINARIES_ONLY);
    basedContentDefinitionProvider.addRootPath(System.getProperty("user.dir") + File.separatorChar + "target",
        ResourceType.BINARY);

    _systemDefinition.addContentDefinitionProvider(basedContentDefinitionProvider);

    basedContentDefinitionProvider = new FileBasedContentDefinitionProvider("TestName", "5.6.7.8",
        AnalyzeMode.BINARIES_ONLY);
    basedContentDefinitionProvider.addRootPath(System.getProperty("user.dir") + File.separatorChar + "target",
        ResourceType.BINARY);

    _systemDefinition.addContentDefinitionProvider(basedContentDefinitionProvider);
  }

  /**
   * <p>
   * </p>
   * 
   * @throws IOException
   */
  @Test
  public void testSystemDefinition() throws IOException {

    //
    try (FileWriter fileWriter = new FileWriter(_targetFile)) {
      new SystemDefinitionFactory().save(_systemDefinition, fileWriter);
    }

    //
    ISystemDefinition systemDefinition = null;
    try (FileReader fileReader = new FileReader(_targetFile)) {
      systemDefinition = new SystemDefinitionFactory().loadSystemDefinition(fileReader,
          this.getClass().getClassLoader(), null);
    }

    //
    Assert.assertNotNull(systemDefinition);
    Assert.assertEquals(2, systemDefinition.getContentDefinitionProviders().size());

    //
    systemDefinition.initialize(null);

    //
    assertThat(systemDefinition.getContentDefinitions().get(0).getId()).isEqualTo("0000000000-1");
    assertThat(systemDefinition.getContentDefinitions().get(0).getName()).isEqualTo("TestName");
    assertThat(systemDefinition.getContentDefinitions().get(0).getVersion()).isEqualTo("1.2.3.4");

    assertThat(systemDefinition.getContentDefinitions().get(1).getId()).isEqualTo("0000000001-1");
    assertThat(systemDefinition.getContentDefinitions().get(1).getName()).isEqualTo("TestName");
    assertThat(systemDefinition.getContentDefinitions().get(1).getVersion()).isEqualTo("5.6.7.8");
  }
}
