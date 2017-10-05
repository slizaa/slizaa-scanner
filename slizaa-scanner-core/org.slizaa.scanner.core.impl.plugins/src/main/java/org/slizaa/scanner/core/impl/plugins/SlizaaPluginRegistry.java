package org.slizaa.scanner.core.impl.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.UserFunction;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;
import org.slizaa.scanner.core.spi.parser.IParserFactory;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class SlizaaPluginRegistry implements ISlizaaPluginRegistry {

  /** - */
  private List<IClassAnnotationMatchProcessor>  _classAnnotationMatchProcessors;

  /** - */
  private List<IMethodAnnotationMatchProcessor> _methodAnnotationMatchProcessors;

  /** - */
  private Map<Class<?>, List<?>>                _codeSourceToScan;

  /**
   * 
   */
  public SlizaaPluginRegistry() {

    //
    _classAnnotationMatchProcessors = new ArrayList<>();
    _methodAnnotationMatchProcessors = new ArrayList<>();
  }

  @Override
  public void registerClassAnnotationMatchProcessor(IClassAnnotationMatchProcessor processor) {

    //
    if (!_classAnnotationMatchProcessors.contains(processor)) {
      _classAnnotationMatchProcessors.add(processor);
    }
  }

  @Override
  public void registerMethodAnnotationMatchProcessor(IMethodAnnotationMatchProcessor processor) {

    //
    if (!_methodAnnotationMatchProcessors.contains(processor)) {
      _methodAnnotationMatchProcessors.add(processor);
    }
  }

  @Override
  public <T> void registerCodeSourceToScan(Class<T> type, T codeSource) {
    // TODO Auto-generated method stub

  }

  @Override
  public <T> void unregisterCodeSourceToScan(Class<T> type, T codeSource) {
    // TODO Auto-generated method stub

  }

  @Override
  public <T> void registerCodeSourceClassLoaderProvider(Class<T> type, Function<?, ClassLoader> classLoaderProvider) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public <T> void unregisterCodeSourceClassLoaderProvider(Class<T> type, Function<?, ClassLoader> classLoaderProvider) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public <T> void rescan(Class<T> type) {
    // TODO Auto-generated method stub
    
  }

  /**
   * <p>
   * </p>
   */
  public void initialize() {

    //
    IClassAnnotationMatchProcessor matchProcessor = new IClassAnnotationMatchProcessor() {

      @Override
      public Class<?> getAnnotationToMatch() {
        return SlizaaParserFactory.class;
      }

      @Override
      public void scanStart(Object object) {
        // TODO Auto-generated method stub

      }

      @Override
      public void consume(Class<?> classWithAnnotation) {
        System.out.println(classWithAnnotation.getName());

      }

      @Override
      public void scanStop(Object object) {
        // TODO Auto-generated method stub

      }
    };

    //
    new FastClasspathScanner("-org.slizaa.scanner.core.spi.parser", "-org.neo4j.kernel.builtinprocs",
        "-org.neo4j.kernel.impl.proc", "-org.neo4j.server.security.auth")

            // set verbose
            // .verbose()

            // set the classloader to scan
            .overrideClassLoaders(_classLoaders.toArray(new ClassLoader[0]))

            //
            .matchClassesImplementing(IParserFactory.class, matchingClass -> {
              _parserFactories.add(matchingClass);
            })

            //
            .matchClassesWithAnnotation(matchProcessor.getAnnotationToMatch(),
                classWithAnnotation -> matchProcessor.consume(classWithAnnotation))

            //
            .matchClassesWithMethodAnnotation(Procedure.class, (matchingClass, matchingMethodOrConstructor) -> {
              if (!_neo4jExtensions.contains(matchingClass)) {
                _neo4jExtensions.add(matchingClass);
              }
            })

            //
            .matchClassesWithMethodAnnotation(UserFunction.class, (matchingClass, matchingMethodOrConstructor) -> {
              if (!_neo4jExtensions.contains(matchingClass)) {
                _neo4jExtensions.add(matchingClass);
              }
            })

            // Actually perform the scan (nothing will happen without this call)
            .scan();
  }
}
