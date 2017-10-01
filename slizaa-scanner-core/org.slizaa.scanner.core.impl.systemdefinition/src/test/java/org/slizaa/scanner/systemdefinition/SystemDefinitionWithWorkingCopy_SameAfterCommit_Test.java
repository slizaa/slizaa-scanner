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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinitionWithWorkingCopy;

public class SystemDefinitionWithWorkingCopy_SameAfterCommit_Test {

  /**
   * <p>
   * </p>
   * 
   */
  @Test
  public void testCommitWorkingCopy() {

    //
    SystemDefinitionWithWorkingCopy systemDefinition = TestHelper.createDefaultSystemDefinitionWithWorkingCopy();
    ITempDefinitionProvider prov_1 = systemDefinition.getContentDefinitionProviders().get(0);
    ITempDefinitionProvider prov_2 = systemDefinition.getContentDefinitionProviders().get(1);

    //
    systemDefinition.startWorkingCopy();

    ITempDefinitionProvider prov_1_copy = systemDefinition.getContentDefinitionProviders().get(0);
    ITempDefinitionProvider prov_2_copy = systemDefinition.getContentDefinitionProviders().get(1);
    
    assertSame(prov_1, prov_1_copy);
    assertSame(prov_2, prov_2_copy);
    assertEquals(prov_1, prov_1_copy);
    assertEquals(prov_2, prov_2_copy);

    ((FileBasedContentDefinitionProvider) systemDefinition.getContentDefinitionProviders().get(0)).setVersion("1");
    ((FileBasedContentDefinitionProvider) systemDefinition.getContentDefinitionProviders().get(1)).setVersion("11");

    systemDefinition.commitWorkingCopy();

    //
    assertSame(prov_1, systemDefinition.getContentDefinitionProviders().get(0));
    assertSame(prov_2, systemDefinition.getContentDefinitionProviders().get(1));
    assertEquals(prov_1, prov_1_copy);
    assertEquals(prov_2, prov_2_copy);
    
    assertThat(((FileBasedContentDefinitionProvider) systemDefinition.getContentDefinitionProviders().get(0)).getVersion(), is("1"));
    assertThat(((FileBasedContentDefinitionProvider) systemDefinition.getContentDefinitionProviders().get(1)).getVersion(), is("11"));
  }
}
