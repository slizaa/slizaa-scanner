package org.slizaa.scanner.core.impl.plugins;


import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;

import com.google.common.base.Stopwatch;

public class PluginRegistryTest {

  /**
   * <p>
   * </p>
   * 
   * @throws ClassNotFoundException
   */
  @Test
  public void testPluginRegistry() throws ClassNotFoundException {

    //
    SlizaaPluginRegistry pluginRegistry = new SlizaaPluginRegistry();

    ClassLoader classLoader = new URLClassLoader(
        new URL[] { this.getClass().getProtectionDomain().getCodeSource().getLocation() });

    pluginRegistry.registerCodeSourceToScan(ClassLoader.class, classLoader);
    pluginRegistry.registerCodeSourceClassLoaderProvider(ClassLoader.class, cl -> cl);

    //
    DefaultClassAnnotationMatchProcessor processor = new DefaultClassAnnotationMatchProcessor(
        SlizaaParserFactory.class);
    pluginRegistry.registerClassAnnotationMatchProcessor(processor);

    //
    Stopwatch stopwatch = Stopwatch.createStarted();
    pluginRegistry.scan();

    assertThat(processor.getCollectedClasses()).containsExactly(DummyParserFactory.class);
    System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
  }

  // @Test
  // public void test1() {
  //
  // //
  // System.out.println(SlizaaPluginUtils.getExtensionsFromClasspath());
  // }
}
