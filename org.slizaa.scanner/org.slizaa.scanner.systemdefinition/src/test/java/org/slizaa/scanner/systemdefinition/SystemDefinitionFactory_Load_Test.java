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
package org.slizaa.scanner.systemdefinition;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class SystemDefinitionFactory_Load_Test {

  /**
   * <p>
   * </p>
   * 
   * @throws IOException
   */
  @Test
  public void testSystemDefinition() throws IOException {

    // create target file
    File targetFile = new File(String.format("%s/target/junit/test_%s.json", System.getProperty("user.dir"),
        System.currentTimeMillis()));
    targetFile.getParentFile().mkdirs();

    // persist target definition
    ISystemDefinition systemDefinition2 = TestHelper.createDefaultSystemDefinition();
    systemDefinition2.addContentDefinitionProvider(TestHelper.createContentDefinitionProvider());
    systemDefinition2.addContentDefinitionProvider(TestHelper.createContentDefinitionProvider());
    try (FileWriter fileWriter = new FileWriter(targetFile)) {
      new SystemDefinitionFactory().save(systemDefinition2, fileWriter);
    }

    // load target definition
    try (FileReader fileReader = new FileReader(targetFile)) {

      ISystemDefinition systemDefinition = new SystemDefinitionFactory().loadSystemDefinition(fileReader, this
          .getClass().getClassLoader(), null);

      assertThat(systemDefinition.getContentDefinitionProviders()).hasSize(4);

      //
      systemDefinition.addSystemChangedListener(new ISystemDefinitionChangedListener() {
        @Override
        public void systemDefinitionChanged(SystemDefinitionChangedEvent event) {
          System.out.println("HURZ");
        }
      });
      
      FileBasedContentDefinitionProvider provider = (FileBasedContentDefinitionProvider) systemDefinition
          .getContentDefinitionProviders().get(0);
      provider.setVersion("123");

    }
  }
}
