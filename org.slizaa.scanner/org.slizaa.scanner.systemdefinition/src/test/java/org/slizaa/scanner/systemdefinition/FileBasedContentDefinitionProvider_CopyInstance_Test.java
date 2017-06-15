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

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class FileBasedContentDefinitionProvider_CopyInstance_Test {

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {

    //
    FileBasedContentDefinitionProvider provider_1 = TestHelper.createContentDefinitionProvider();
    FileBasedContentDefinitionProvider provider_1_copy = (FileBasedContentDefinitionProvider) provider_1.copyInstance();

    //
    assertThat(provider_1 != provider_1_copy).isTrue();
    assertThat(provider_1).isEqualTo(provider_1_copy);
    assertThat(provider_1.equals(provider_1_copy)).isTrue();

    //
    FileBasedContentDefinitionProvider provider_2 = TestHelper.createContentDefinitionProvider();
    FileBasedContentDefinitionProvider provider_2_copy = (FileBasedContentDefinitionProvider) provider_2.copyInstance();

    //
    assertThat(provider_2 != provider_2_copy).isTrue();
    assertThat(provider_2).isEqualTo(provider_2_copy);
    assertThat(provider_2.equals(provider_1_copy)).isTrue();

    //
    Set<FileBasedContentDefinitionProvider> set_1 = new HashSet<>();
    Set<FileBasedContentDefinitionProvider> set_2 = new HashSet<>();
    set_1.add(provider_1);
    set_2.add(provider_2);
    set_2.add(provider_1_copy);
    set_2.add(provider_2_copy);

    //
    assertThat(set_1.equals(set_2)).isTrue();
    assertThat(set_2.equals(set_1)).isTrue();
  }
}
