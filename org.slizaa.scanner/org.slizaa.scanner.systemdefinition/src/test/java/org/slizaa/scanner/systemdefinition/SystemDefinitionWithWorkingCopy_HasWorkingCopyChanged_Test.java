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
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinitionWithWorkingCopy;

public class SystemDefinitionWithWorkingCopy_HasWorkingCopyChanged_Test {

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {

    //
    SystemDefinitionWithWorkingCopy systemDefinition = TestHelper.createDefaultSystemDefinitionWithWorkingCopy();

    //
    assertThat(systemDefinition.getContentDefinitionProviders()).hasSize(2);
    assertThat(systemDefinition.isWorkingCopy()).isFalse();

    //
    systemDefinition.startWorkingCopy();
    assertThat(systemDefinition.isWorkingCopy()).isTrue();
    assertThat(systemDefinition.hasWorkingCopyChanged()).isFalse();

    systemDefinition
        .addContentDefinitionProvider(new FileBasedContentDefinitionProvider("Z", "1", AnalyzeMode.BINARIES_ONLY));
    assertThat(systemDefinition.getContentDefinitionProviders()).hasSize(3);

    //
    assertThat(systemDefinition.hasWorkingCopyChanged()).isTrue();
  }
}
