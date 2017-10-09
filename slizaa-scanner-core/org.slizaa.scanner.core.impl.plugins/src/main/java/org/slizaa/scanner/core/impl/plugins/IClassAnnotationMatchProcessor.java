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
   */
  void added(Object codeSource);

  /**
   * <p>
   * </p>
   *
   * @param codeSource
   * @param classesWithAnnotation
   */
  void changed(Object codeSource, List<Class<?>> classesWithAnnotation);

  /**
   * <p>
   * </p>
   *
   * @param codeSource
   */
  void removed(Object codeSource);
}
