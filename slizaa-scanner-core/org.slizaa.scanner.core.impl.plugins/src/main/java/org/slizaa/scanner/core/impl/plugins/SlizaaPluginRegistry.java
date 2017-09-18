package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import org.slizaa.scanner.spi.parser.IParserFactory;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class SlizaaPluginRegistry {

  /** - */
  private List<String>                          _serverExtensions;

  /** - */
  private List<Class<? extends IParserFactory>> _parserFactories;

  /** - */
  private List<ClassLoader>                     _classLoaders;

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaPluginRegistry}.
   * </p>
   *
   */
  public SlizaaPluginRegistry(List<ClassLoader> classLoaders) {

    //
    _classLoaders = checkNotNull(classLoaders);
    _parserFactories = new LinkedList<>();
  }

  /**
   * <p>
   * </p>
   */
  public void initialize() {

    //
    new FastClasspathScanner("-org.slizaa.scanner.spi.parser")

        // set verbose
        // .verbose()

        // set the classloader to scan
        .overrideClassLoaders(_classLoaders.toArray(new ClassLoader[0]))

        //
        .matchClassesImplementing(IParserFactory.class, matchingClass -> {
          _parserFactories.add(matchingClass);
        })

        // Actually perform the scan (nothing will happen without this call)
        .scan();

    //
    System.out.println(_parserFactories);

    //
    // try {
    // Enumeration<URL> fileUrls = this.getClass().getClassLoader().getResources("slizaa-scanner-plugin.json");
    // while (fileUrls.hasMoreElements()) {
    // URL fileUrl = fileUrls.nextElement();
    // System.out.println(fileUrl);
    // }
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
  }
}
