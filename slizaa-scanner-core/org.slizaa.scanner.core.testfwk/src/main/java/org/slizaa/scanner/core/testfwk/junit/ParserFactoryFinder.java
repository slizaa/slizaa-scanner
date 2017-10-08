package org.slizaa.scanner.core.testfwk.junit;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slizaa.scanner.core.impl.plugins.IClassAnnotationMatchProcessor;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ParserFactoryFinder implements IClassAnnotationMatchProcessor {

  /** - */
  private Map<Object, List<Class<?>>> _classesWithAnnotation = new HashMap<>();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<Class<?>> getAllClassesWithAnnotation() {
    return _classesWithAnnotation.values().stream().flatMap(list -> list.stream()).collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends Annotation> getAnnotationToMatch() {
    return SlizaaParserFactory.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void consume(Object codeSource, ClassLoader classLoader, List<Class<?>> classesWithAnnotation) {
    _classesWithAnnotation.put(codeSource, classesWithAnnotation);
  }
}
