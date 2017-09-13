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

import org.junit.Test;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinitionWithWorkingCopy;

public class SystemDefinitionWithWorkingCopy_EditContentProvider_Test {

  @Test
  public void testCommitWorkingCopy() {

    //
    SystemDefinitionWithWorkingCopy systemDefinition = TestHelper.createDefaultSystemDefinitionWithWorkingCopy();

    resetContentProviderVersion(systemDefinition, "1.2.3.4", "1");
    resetContentProviderVersion(systemDefinition, "1", "2");
    resetContentProviderVersion(systemDefinition, "2", "3");
    resetContentProviderVersion(systemDefinition, "3", "4");
    resetContentProviderVersion(systemDefinition, "4", "5");
  }

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param version
   */
  private void resetContentProviderVersion(SystemDefinitionWithWorkingCopy systemDefinition, String oldVersion,
      String newVersion) {

    //
    FileBasedContentDefinitionProvider provider_before_startWC = (FileBasedContentDefinitionProvider) systemDefinition
        .getContentDefinitionProviders().get(0);

    assertThat(provider_before_startWC.getVersion()).isEqualTo(oldVersion);

    //
    systemDefinition.startWorkingCopy();

    //
    FileBasedContentDefinitionProvider provider_after_startWC = (FileBasedContentDefinitionProvider) systemDefinition
        .getContentDefinitionProviders().get(0);

    assertThat(provider_after_startWC.getVersion()).isEqualTo(oldVersion);

    //
    provider_after_startWC.setVersion(newVersion);

    //
    systemDefinition.commitWorkingCopy();

    //
    FileBasedContentDefinitionProvider provider_after_commitWC = (FileBasedContentDefinitionProvider) systemDefinition
        .getContentDefinitionProviders().get(0);

    System.out.println(provider_before_startWC + " : " + provider_after_startWC + " : " + provider_after_commitWC);

    assertThat(provider_after_commitWC.getVersion()).isEqualTo(newVersion);
  }
}
