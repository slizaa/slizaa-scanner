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

import org.junit.Before;
import org.junit.Test;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinitionWithWorkingCopy;

public class SystemDefinitionWithWorkingCopy_AssertWorkingCopy_Test {

  /** - */
  private SystemDefinitionWithWorkingCopy _systemDefinitionWithWorkingCopy;

  @Before
  public void initialize() {
    _systemDefinitionWithWorkingCopy = TestHelper.createDefaultSystemDefinitionWithWorkingCopy();
  }

  @Test
  public void test_1() {
    _systemDefinitionWithWorkingCopy.addContentDefinitionProvider(TestHelper.createContentDefinitionProvider());
  }

  @Test
  public void test_2() {
    _systemDefinitionWithWorkingCopy.addContentDefinitionProvider(TestHelper.createContentDefinitionProvider(),
        TestHelper.createContentDefinitionProvider());
  }

  @Test
  public void test_3() {
    _systemDefinitionWithWorkingCopy.removeContentDefinitionProvider(TestHelper.createContentDefinitionProvider());
  }

  @Test
  public void test_4() {
    _systemDefinitionWithWorkingCopy.removeContentDefinitionProvider(TestHelper.createContentDefinitionProvider(),
        TestHelper.createContentDefinitionProvider());
  }

  @Test
  public void test_5() {
    _systemDefinitionWithWorkingCopy.clearContentDefinitionProviders();
  }

  @Test
  public void test_6() {
    _systemDefinitionWithWorkingCopy.moveDownContentDefinitionProvider(_systemDefinitionWithWorkingCopy
        .getContentDefinitionProviders().get(0));
  }

  @Test
  public void test_7() {
    _systemDefinitionWithWorkingCopy.moveUpContentDefinitionProvider(_systemDefinitionWithWorkingCopy
        .getContentDefinitionProviders().get(1));
  }
}
