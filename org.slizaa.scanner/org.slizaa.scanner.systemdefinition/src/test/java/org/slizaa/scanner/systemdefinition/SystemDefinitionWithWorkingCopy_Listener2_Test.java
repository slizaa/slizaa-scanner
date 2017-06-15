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
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinitionWithWorkingCopy;

public class SystemDefinitionWithWorkingCopy_Listener2_Test {

  /** - */
  private boolean _eventSent = false;

  /**
   * <p>
   * </p>
   * 
   */
  @Test
  public void test() {

    //
    SystemDefinitionWithWorkingCopy systemDefinition = TestHelper.createDefaultSystemDefinitionWithWorkingCopy();

    systemDefinition.addSystemChangedListener(new ISystemDefinitionChangedListener() {
      @Override
      public void systemDefinitionChanged(SystemDefinitionChangedEvent event) {
        _eventSent = true;
      }
    });

    //
    systemDefinition.startWorkingCopy();
    assertThat(_eventSent, is(false));
    systemDefinition.discardWorkingCopy();
    assertThat(_eventSent, is(false));
    systemDefinition.startWorkingCopy();
    assertThat(_eventSent, is(false));

    //
    FileBasedContentDefinitionProvider provider = (FileBasedContentDefinitionProvider) systemDefinition
        .getContentDefinitionProviders().get(0);

    assertThat(_eventSent, is(false));
    provider.setVersion("hahhl");
    assertThat(_eventSent, is(true));
  }
}
