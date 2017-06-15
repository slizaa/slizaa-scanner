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

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.slizaa.scanner.systemdefinition.IResourceChangedListener;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.ISystemDefinitionChangedListener;
import org.slizaa.scanner.systemdefinition.ResourceChangedEvent;
import org.slizaa.scanner.systemdefinition.SystemDefinitionChangedEvent;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

public abstract class AbstractSystemChangedTest {

  /** - */
  private List<ResourceChangedEvent>         _resourceChangedEvents;

  /** - */
  private List<SystemDefinitionChangedEvent> _systemDefinitionChangedEvents;

  /** - */
  private ISystemDefinition                  _systemDefinition;

  @Before
  public void before() {

    //
    _resourceChangedEvents = new LinkedList<ResourceChangedEvent>();
    _systemDefinitionChangedEvents = new LinkedList<SystemDefinitionChangedEvent>();

    _systemDefinition = new SystemDefinitionFactory().createNewSystemDefinition();
    
    _systemDefinition.addSystemChangedListener(new ISystemDefinitionChangedListener() {
      @Override
      public void systemDefinitionChanged(SystemDefinitionChangedEvent event) {
        _systemDefinitionChangedEvents.add(event);
      }
    });
    
    _systemDefinition.addResourceChangedListener(new IResourceChangedListener() {
      @Override
      public void resourceChanged(ResourceChangedEvent event) {
        _resourceChangedEvents.add(event);
      }
    });
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public final List<ResourceChangedEvent> resourceChangedEvents() {
    return _resourceChangedEvents;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public final List<SystemDefinitionChangedEvent> systemDefinitionChangedEvents() {
    return _systemDefinitionChangedEvents;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public final ISystemDefinition systemDefinition() {
    return _systemDefinition;
  }
}
