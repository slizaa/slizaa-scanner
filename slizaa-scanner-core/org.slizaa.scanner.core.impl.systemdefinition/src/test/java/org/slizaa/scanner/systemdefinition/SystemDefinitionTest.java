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
import static org.slizaa.scanner.systemdefinition.TestHelper.createContentDefinitionProvider;

import org.junit.Before;
import org.junit.Test;

public class SystemDefinitionTest {

  /** - */
  private ISystemDefinition _systemDefinition;

  @Before
  public void before() {
    _systemDefinition = new SystemDefinitionFactory().createNewSystemDefinition();
  }

  @Test(expected = IllegalStateException.class)
  public void testInitializedTest() {
    _systemDefinition.addContentDefinitionProvider(createContentDefinitionProvider());
    _systemDefinition.getContentDefinitions();
  }

  @Test
  public void testAddRemoveProviders() {

    //
    IContentDefinitionProvider provider_1 = createContentDefinitionProvider();
    IContentDefinitionProvider provider_2 = createContentDefinitionProvider();
    IContentDefinitionProvider provider_3 = createContentDefinitionProvider();

    //
    _systemDefinition.addContentDefinitionProvider(provider_1);
    assertThat(_systemDefinition.getContentDefinitionProviders()).hasSize(1);

    //
    _systemDefinition.addContentDefinitionProvider(provider_2);
    assertThat(_systemDefinition.getContentDefinitionProviders()).hasSize(2);

    //
    _systemDefinition.addContentDefinitionProvider(provider_3);
    assertThat(_systemDefinition.getContentDefinitionProviders()).hasSize(3);

    //
    _systemDefinition.addContentDefinitionProvider(provider_1);
    _systemDefinition.addContentDefinitionProvider(provider_2);
    _systemDefinition.addContentDefinitionProvider(provider_3);
    assertThat(_systemDefinition.getContentDefinitionProviders()).hasSize(3);

    //
    _systemDefinition.removeContentDefinitionProvider(provider_1);
    _systemDefinition.removeContentDefinitionProvider(provider_2);
    assertThat(_systemDefinition.getContentDefinitionProviders()).hasSize(1);

    //
    _systemDefinition.removeContentDefinitionProvider(provider_3.getId());
    assertThat(_systemDefinition.getContentDefinitionProviders()).hasSize(0);
  }

  @Test
  public void testReinitialize() {

    //
    assertThat(_systemDefinition.isInitialized()).isFalse();

    //
    IContentDefinitionProvider provider_1 = createContentDefinitionProvider();
    IContentDefinitionProvider provider_2 = createContentDefinitionProvider();
    IContentDefinitionProvider provider_3 = createContentDefinitionProvider();

    //
    _systemDefinition.addContentDefinitionProvider(provider_1);
    _systemDefinition.addContentDefinitionProvider(provider_2);
    _systemDefinition.addContentDefinitionProvider(provider_3);
    assertThat(_systemDefinition.isInitialized()).isFalse();
    _systemDefinition.initialize(null);
    assertThat(_systemDefinition.isInitialized()).isTrue();

    //
    _systemDefinition.moveDownContentDefinitionProvider(provider_1, provider_2);
    assertThat(_systemDefinition.isInitialized()).isFalse();
    _systemDefinition.initialize(null);
    assertThat(_systemDefinition.isInitialized()).isTrue();

    //
    _systemDefinition.moveUpContentDefinitionProvider(provider_1);
    assertThat(_systemDefinition.isInitialized()).isFalse();
    _systemDefinition.initialize(null);
    assertThat(_systemDefinition.isInitialized()).isTrue();

    //
    _systemDefinition.removeContentDefinitionProvider(provider_1);
    assertThat(_systemDefinition.isInitialized()).isFalse();
    _systemDefinition.initialize(null);
    assertThat(_systemDefinition.isInitialized()).isTrue();

    //
    _systemDefinition.removeContentDefinitionProvider(provider_2.getId());
    assertThat(_systemDefinition.isInitialized()).isFalse();
    _systemDefinition.initialize(null);
    assertThat(_systemDefinition.isInitialized()).isTrue();

    //
    _systemDefinition.clearContentDefinitionProviders();
    assertThat(_systemDefinition.isInitialized()).isFalse();
    _systemDefinition.initialize(null);
    assertThat(_systemDefinition.isInitialized()).isTrue();
  }
}
