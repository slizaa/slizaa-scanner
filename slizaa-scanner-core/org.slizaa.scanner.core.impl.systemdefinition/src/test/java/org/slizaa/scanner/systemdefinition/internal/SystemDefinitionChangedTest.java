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
import static org.slizaa.scanner.systemdefinition.TestHelper.createContentDefinitionProvider;

import java.io.File;

import org.junit.Test;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.spi.content.support.DefaultVariablePath;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;

public class SystemDefinitionChangedTest extends AbstractSystemChangedTest {

  @Test
  public void testAddRemoveContentDefinitionProvider() {

    //
    FileBasedContentDefinitionProvider provider = (FileBasedContentDefinitionProvider) createContentDefinitionProvider();

    //
    systemDefinition().addContentDefinitionProvider(provider);
    assertThat(systemDefinitionChangedEvents()).hasSize(1);

    //
    systemDefinition().removeContentDefinitionProvider(provider);
    assertThat(systemDefinitionChangedEvents()).hasSize(2);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testContentDefinitionProviderChanged() {

    //
    FileBasedContentDefinitionProvider provider = (FileBasedContentDefinitionProvider) createContentDefinitionProvider();
    String srcPath = System.getProperty("user.dir") + File.separatorChar + "src/main/java";

    //
    systemDefinition().addContentDefinitionProvider(provider);
    assertThat(systemDefinitionChangedEvents()).hasSize(1);

    //
    provider.addRootPath(srcPath, ResourceType.SOURCE);
    assertThat(systemDefinitionChangedEvents()).hasSize(2);

    //
    provider.removeRootPath(new DefaultVariablePath(srcPath), ResourceType.SOURCE);
    assertThat(systemDefinitionChangedEvents()).hasSize(3);

    //
    systemDefinition().removeContentDefinitionProvider(provider);
    assertThat(systemDefinitionChangedEvents()).hasSize(4);

    // NO FURTHER CHANGES!
    provider.addRootPath(srcPath, ResourceType.SOURCE);
    assertThat(systemDefinitionChangedEvents()).hasSize(4);
  }
}
