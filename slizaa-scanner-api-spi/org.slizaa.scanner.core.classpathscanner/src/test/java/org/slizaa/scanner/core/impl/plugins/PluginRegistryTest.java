package org.slizaa.scanner.core.impl.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
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
    ClassLoader pathToScan = new URLClassLoader(
        new URL[] { this.getClass().getProtectionDomain().getCodeSource().getLocation() });

    //
    IClasspathScannerFactory pluginRegistry = new ClasspathScannerFactory()
        .registerCodeSourceClassLoaderProvider(ClassLoader.class, cl -> cl);

    //
    List<IParserFactory> parserFactories = new ArrayList<>();

    //
    pluginRegistry.createScanner(pathToScan).matchClassesWithAnnotation(SlizaaParserFactory.class, (codeSource, classes) -> {

      //
      for (Class<?> clazz : classes) {
        try {
          parserFactories.add((IParserFactory) clazz.newInstance());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    }).scan();

    //
    Stopwatch stopwatch = Stopwatch.createStarted();

    //
    assertThat(parserFactories).hasSize(1);
    System.out.println(stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
  }
}
