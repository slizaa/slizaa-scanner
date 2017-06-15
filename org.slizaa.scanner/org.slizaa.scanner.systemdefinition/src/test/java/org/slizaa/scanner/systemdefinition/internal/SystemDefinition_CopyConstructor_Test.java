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

import org.junit.Test;
import org.slizaa.scanner.systemdefinition.TestHelper;

public class SystemDefinition_CopyConstructor_Test {

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {

    // create original and copy
    SystemDefinition originalDefinition = TestHelper.createDefaultSystemDefinition();
    SystemDefinition copyDefinition = new SystemDefinition(originalDefinition);

    // tests
    assertThat(originalDefinition == copyDefinition).isFalse();
    assertThat(originalDefinition.getCurrentId()).isEqualTo(copyDefinition.getCurrentId());

    assertThat(originalDefinition.getContentDefinitionProviders() == copyDefinition.getContentDefinitionProviders())
        .isFalse();
    assertThat(originalDefinition.getContentDefinitionProviders().size())
        .isEqualTo(copyDefinition.getContentDefinitionProviders().size());

    //
    assertThat(originalDefinition.getContentDefinitionProviders())
        .isEqualTo(copyDefinition.getContentDefinitionProviders());
  }
}
