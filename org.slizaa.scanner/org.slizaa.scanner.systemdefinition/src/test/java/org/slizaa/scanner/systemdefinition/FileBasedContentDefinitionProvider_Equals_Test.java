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
import org.slizaa.scanner.importer.spi.content.AnalyzeMode;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinition;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinitionWithWorkingCopy;

public class FileBasedContentDefinitionProvider_Equals_Test {

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {
    //
    SystemDefinition systemDefinition = TestHelper.createDefaultSystemDefinition();
    SystemDefinitionWithWorkingCopy workingCopy = TestHelper
        .createDefaultSystemDefinitionWithWorkingCopy(systemDefinition);

    //
    workingCopy.startWorkingCopy();

    //
    FileBasedContentDefinitionProvider provider = (FileBasedContentDefinitionProvider) workingCopy
        .getContentDefinitionProviders().get(0);

    assertThat(provider.getAnalyzeMode()).isEqualTo(AnalyzeMode.BINARIES_ONLY);
    provider.setAnalyzeMode(AnalyzeMode.BINARIES_AND_SOURCES);
    assertThat(provider.getAnalyzeMode()).isEqualTo(AnalyzeMode.BINARIES_AND_SOURCES);

    //
    assertThat(workingCopy.hasWorkingCopyChanged()).isTrue();
    workingCopy.commitWorkingCopy();
    assertThat(workingCopy.hasWorkingCopyChanged()).isFalse();
  }
}
