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

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinitionWithWorkingCopy;

public class SystemDefinitionWithWorkingCopy_Listener_Test {

  /** - */
  private static List<SystemDefinitionChangedEvent> _systemDefinitionChangedEvents;

  /** - */
  private static SystemDefinitionWithWorkingCopy    _systemDefinitionWithWorkingCopy;

  private ISystemDefinitionChangedListener          _listener;

  @BeforeClass
  public static void initializeClass() {
    _systemDefinitionWithWorkingCopy = TestHelper.createDefaultSystemDefinitionWithWorkingCopy();
    _systemDefinitionChangedEvents = new LinkedList<>();
  }

  /**
   * <p>
   * </p>
   */
  @Before
  public void initialize() {

    _listener = new ISystemDefinitionChangedListener() {
      @Override
      public void systemDefinitionChanged(SystemDefinitionChangedEvent event) {
        _systemDefinitionChangedEvents.add(event);
      }
    };

    //
    _systemDefinitionWithWorkingCopy.addSystemChangedListener(_listener);
  }

  @After
  public void dispose() {
    _systemDefinitionWithWorkingCopy.removeSystemChangedListener(_listener);
  }

  /**
   * <p>
   * </p>
   */
  protected void assertSystemDefinitionChangedEvent() {
    assertThat(_systemDefinitionChangedEvents).hasSize(1);
    _systemDefinitionChangedEvents.clear();
  }

  /**
   * <p>
   * </p>
   */
  protected void assertNoSystemDefinitionChangedEvent() {
    assertThat(_systemDefinitionChangedEvents).hasSize(0);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testCommit() {
    _addProvider(2, true);
  }

  @Test
  public void testDiscard() {
    _addProvider(2, false);
  }

  @Test
  public void testDiscardAndCommit() {
    _addProvider(3, false);
    _addProvider(4, true);
  }

  @Test
  public void testMultipleCommits() {
    _addProvider(2, false);
    _addProvider(5, true);
    _addProvider(7, true);
  }

  /**
   * <p>
   * </p>
   * 
   * @param providerCount
   * @param offsetWorkingCopyChangedEvents
   * @param offsetSystemDefinitionWithWorkingCopy
   */
  private void _addProvider(int providerCount, boolean commit) {

    //
    _systemDefinitionWithWorkingCopy.startWorkingCopy();

    //
    for (int i = 1; i <= providerCount; i++) {
      _systemDefinitionWithWorkingCopy.addContentDefinitionProvider(TestHelper.createContentDefinitionProvider());
      assertSystemDefinitionChangedEvent();
    }

    //
    if (commit) {
      _systemDefinitionWithWorkingCopy.commitWorkingCopy();
      assertSystemDefinitionChangedEvent();
    }
    //
    else {
      _systemDefinitionWithWorkingCopy.discardWorkingCopy();
      assertSystemDefinitionChangedEvent();
    }
  }
}
