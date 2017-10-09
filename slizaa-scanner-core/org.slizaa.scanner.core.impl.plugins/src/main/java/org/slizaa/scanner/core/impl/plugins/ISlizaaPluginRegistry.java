package org.slizaa.scanner.core.impl.plugins;

/**
 * <p>
 * </p>
 */
public interface ISlizaaPluginRegistry {

  /** - */
  public static final String SLIZAA_EXTENSION_HEADER = "Slizaa-Extension";

  /**
   * <p>
   * </p>
   *
   * @param processor
   * @return
   */
  <T extends IClassAnnotationMatchProcessor> T registerClassAnnotationMatchProcessor(T processor);

  /**
   * <p>
   * </p>
   *
   * @param processor
   * @return
   */
  <T extends IMethodAnnotationMatchProcessor> T registerMethodAnnotationMatchProcessor(T processor);

  /**
   * @param type
   */
  <T> void rescan(Class<T> type);

  /**
   * @param type
   * @param codeSource
   */
  <T> void rescan(Class<T> type, T codeSource);
}
