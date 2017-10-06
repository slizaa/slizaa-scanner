package org.slizaa.scanner.core.impl.plugins;

import java.lang.annotation.Annotation;
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

    pluginRegistry.registerClassAnnotationMatchProcessor(new IClassAnnotationMatchProcessor() {

      @Override
      public Class<? extends Annotation> getAnnotationToMatch() {
        return SlizaaParserFactory.class;
      }

      @Override
      public void scanStart(Object object) {
        // TODO Auto-generated method stub
      }

      @Override
      public void scanStop(Object object) {
        // TODO Auto-generated method stub
      }

      @Override
      public void consume(Class<?> classWithAnnotation) {
        System.out.println("classWithAnnotation: " + classWithAnnotation);

      }
    });

    Stopwatch stopwatch = Stopwatch.createStarted();
    pluginRegistry.scan(ClassLoader.class);
    System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));

    // //
    // assertThat(pluginRegistry.getNeo4jExtensions()).containsOnly(DummyProceduresClass.class,
    // DummyFunctionsClass.class);
    //
    // //
    // assertThat(pluginRegistry.getParserFactories()).containsExactly(DummyParserFactory.class);
  }
}
