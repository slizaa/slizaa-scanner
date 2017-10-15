package org.slizaa.scanner.core.impl.plugins;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IClasspathScanner {

  /**
   * <p>
   * </p>
   *
   * @param clazz
   * @param processor
   * @return
   */
  IClasspathScanner matchClassesWithAnnotation(Class<?> clazz, IClassAnnotationMatchProcessor processor);

  /**
   * <p>
   * </p>
   *
   */
  void scan();
}