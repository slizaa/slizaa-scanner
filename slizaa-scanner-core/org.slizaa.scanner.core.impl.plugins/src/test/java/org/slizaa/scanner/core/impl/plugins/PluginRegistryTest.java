package org.slizaa.scanner.core.impl.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;
import org.slizaa.scanner.core.spi.parser.IParserFactory;

import com.google.common.base.Stopwatch;

public class PluginRegistryTest {

  /**
   * <p>
   * </p>
   * 
   * @throws ClassNotFoundException
   */
  @Test
  public void testPluginRegistry_1() throws ClassNotFoundException {

    // create test class loader
    ClassLoader classLoader = new URLClassLoader(
        new URL[] { this.getClass().getProtectionDomain().getCodeSource().getLocation() });

    //
    SlizaaPluginRegistry pluginRegistry = new SlizaaPluginRegistry()
        .registerCodeSourceClassLoaderProvider(ClassLoader.class, cl -> cl);

    //
    DefaultClassAnnotationMatchProcessor<IParserFactory> processor = pluginRegistry
        .registerClassAnnotationMatchProcessor(new DefaultClassAnnotationMatchProcessor<IParserFactory>(
            SlizaaParserFactory.class, cl -> (IParserFactory) cl.newInstance()));

    //
    Stopwatch stopwatch = Stopwatch.createStarted();
    pluginRegistry.registerCodeSourceToScan(ClassLoader.class, classLoader);

    //
    assertThat(processor.getCollectedClasses()).hasSize(1);
    System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
  }

  /**
   * <p>
   * </p>
   *
   * @throws ClassNotFoundException
   */
  @Test
  public void testPluginRegistry_2() throws ClassNotFoundException {

    // create test class loader
    ClassLoader classLoader = new URLClassLoader(
        new URL[] { this.getClass().getProtectionDomain().getCodeSource().getLocation() });

    //
    SlizaaPluginRegistry pluginRegistry = new SlizaaPluginRegistry()
        .registerCodeSourceClassLoaderProvider(ClassLoader.class, cl -> cl);

    //
    pluginRegistry.registerCodeSourceToScan(ClassLoader.class, classLoader);

    //
    Stopwatch stopwatch = Stopwatch.createStarted();

    //
    DefaultClassAnnotationMatchProcessor<IParserFactory> processor = pluginRegistry
        .registerClassAnnotationMatchProcessor(
            new DefaultClassAnnotationMatchProcessor<IParserFactory>(SlizaaParserFactory.class, cl -> {
              try {
                return (IParserFactory) cl.newInstance();
              } catch (Exception e) {
                return null;
              }
            }));

    //
    assertThat(processor.getCollectedClasses()).hasSize(1);
    System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
  }

  // @Test
  // public void test1() {
  //
  // //
  // System.out.println(SlizaaPluginUtils.getExtensionsFromClasspath());
  // }
}
