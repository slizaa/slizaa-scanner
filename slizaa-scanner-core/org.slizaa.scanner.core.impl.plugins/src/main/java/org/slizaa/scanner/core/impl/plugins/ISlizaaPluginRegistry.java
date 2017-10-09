package org.slizaa.scanner.core.impl.plugins;

/**
 * <p>
 * </p>
 */
public interface ISlizaaPluginRegistry {

  /** - */
  public static final String SLIZAA_EXTENSION_HEADER = "Slizaa-Extension";

  /**
   * @param processor
   */
  ISlizaaPluginRegistry registerClassAnnotationMatchProcessor(IClassAnnotationMatchProcessor processor);

  /**
   * @param processor
   */
  ISlizaaPluginRegistry registerMethodAnnotationMatchProcessor(IMethodAnnotationMatchProcessor processor);

  /**
   */
  <T> void scan();

  /**
   * @param type
   */
  <T> void scan(Class<T> type);

  /**
   * @param type
   * @param codeSource
   */
  <T> void scan(Class<T> type, T codeSource);
}
