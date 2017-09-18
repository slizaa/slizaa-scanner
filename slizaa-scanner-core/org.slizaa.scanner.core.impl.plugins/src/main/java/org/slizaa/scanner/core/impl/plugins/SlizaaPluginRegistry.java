package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;

import org.slizaa.scanner.spi.parser.IParserFactory;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ImplementingClassMatchProcessor;

public class SlizaaPluginRegistry {

  /** - */
  private List<String>      _serverExtensions;

  /** - */
  private List<String>      _parserFactories;

  /** - */
  private List<ClassLoader> _classLoaders;

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaPluginRegistry}.
   * </p>
   *
   */
  public SlizaaPluginRegistry(List<ClassLoader> classLoaders) {

    //
    _classLoaders = checkNotNull(classLoaders);
  }

  /**
   * <p>
   * </p>
   */
  public void initialize() {

    new FastClasspathScanner("-org.slizaa.scanner.spi.parser")
        // .verbose()
        .matchClassesImplementing(IParserFactory.class, new ImplementingClassMatchProcessor<IParserFactory>() {
          @Override
          public void processMatch(Class<? extends IParserFactory> matchingClass) {
            System.out.println("********************************");
            System.out.println("Subclass of Widget: " + matchingClass);
            System.out.println("********************************");
          }
        })
        //
        .overrideClassLoaders(_classLoaders.toArray(new ClassLoader[0]))
        //
        // .registerClassLoaderHandler(EquinoxClassLoaderHandler.class)
        // Actually perform the scan (nothing will happen without this call)
        .scan();

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
