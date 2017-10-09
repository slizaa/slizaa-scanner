package org.slizaa.scanner.core.impl.plugins;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IClassAnnotationMatchProcessor {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  Class<? extends Annotation> getAnnotationToMatch();

  /**
   * <p>
   * </p>
   *
   * @param codeSource
   * @param codeSourceType
   * @param classesWithAnnotation
   */
  void consume(Object codeSource, Class<?> codeSourceType, List<Class<?>> classesWithAnnotation);
}
