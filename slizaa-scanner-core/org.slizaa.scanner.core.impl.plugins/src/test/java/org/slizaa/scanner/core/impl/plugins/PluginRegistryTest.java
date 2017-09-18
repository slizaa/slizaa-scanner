package org.slizaa.scanner.core.impl.plugins;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import org.junit.Test;
import org.slizaa.scanner.spi.parser.IParserFactory;

public class PluginRegistryTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void testPluginRegistry() {

    //
    URL apiURL = IParserFactory.class.getProtectionDomain().getCodeSource().getLocation();
    URL testClassesURL = this.getClass().getProtectionDomain().getCodeSource().getLocation();

    System.out.println(testClassesURL);

    //
    SlizaaPluginRegistry pluginRegistry = new SlizaaPluginRegistry(
        Arrays.asList(new URLClassLoader(new URL[] { apiURL, testClassesURL }, null)));
    pluginRegistry.initialize();
  }
}
