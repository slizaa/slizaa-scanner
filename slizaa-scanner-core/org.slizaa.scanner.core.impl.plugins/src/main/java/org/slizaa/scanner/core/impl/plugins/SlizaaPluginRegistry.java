package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;

import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.UserFunction;
import org.slizaa.scanner.spi.parser.IParserFactory;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

public class SlizaaPluginRegistry {

  /** - */
  private List<Class<?>>                        _graphDbFunctions;

  /** - */
  private List<Class<?>>                        _graphDbProcedures;

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
    _graphDbFunctions = new LinkedList<>();
    _graphDbProcedures = new LinkedList<>();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<Class<?>> getGraphDbUserFunctions() {
    return _graphDbFunctions;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<Class<?>> getGraphDbProcedures() {
    return _graphDbProcedures;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<Class<? extends IParserFactory>> getParserFactories() {
    return _parserFactories;
  }

  /**
   * <p>
   * </p>
   */
  public void initialize() {

    //
    new FastClasspathScanner("-org.slizaa.scanner.spi.parser", "-org.neo4j.kernel.builtinprocs")

        // set verbose
        // .verbose()

        // set the classloader to scan
        .overrideClassLoaders(_classLoaders.toArray(new ClassLoader[0]))

        //
        .matchClassesImplementing(IParserFactory.class, matchingClass -> {
          _parserFactories.add(matchingClass);
        })

        //
        .matchClassesWithMethodAnnotation(Procedure.class, (matchingClass, matchingMethodOrConstructor) -> {
          if (!_graphDbProcedures.contains(matchingClass)) {
            _graphDbProcedures.add(matchingClass);
          }
        })

        //
        .matchClassesWithMethodAnnotation(UserFunction.class, (matchingClass, matchingMethodOrConstructor) -> {
          if (!_graphDbFunctions.contains(matchingClass)) {
            _graphDbFunctions.add(matchingClass);
          }
        })

        // Actually perform the scan (nothing will happen without this call)
        .scan();
  }
}
