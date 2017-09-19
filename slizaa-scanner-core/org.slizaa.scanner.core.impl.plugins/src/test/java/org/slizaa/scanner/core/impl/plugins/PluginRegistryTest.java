package org.slizaa.scanner.core.impl.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class PluginRegistryTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void testPluginRegistry() {

    //
    SlizaaPluginRegistry pluginRegistry = new SlizaaPluginRegistry(
        Arrays.asList(this.getClass().getClassLoader(), null));
    pluginRegistry.initialize();

    //
    assertThat(pluginRegistry.getGraphDbUserFunctions()).containsExactlyInAnyOrder(DummyFunctionsClass.class);

    //
    assertThat(pluginRegistry.getGraphDbProcedures()).containsExactlyInAnyOrder(DummyProceduresClass.class);

    //
    assertThat(pluginRegistry.getParserFactories()).containsExactlyInAnyOrder(DummyParserFactory.class);
  }
}
