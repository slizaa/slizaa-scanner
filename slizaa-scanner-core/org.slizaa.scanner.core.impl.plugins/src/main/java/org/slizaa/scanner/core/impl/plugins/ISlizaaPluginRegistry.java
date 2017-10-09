package org.slizaa.scanner.core.impl.plugins;

import java.util.function.Function;

/**
 * <p>
 * </p>
 */
public interface ISlizaaPluginRegistry {

  /** - */
  public static final String SLIZAA_EXTENSION = "Slizaa-Extension";

  /**
   * @param processor
   */
  ISlizaaPluginRegistry registerClassAnnotationMatchProcessor(IClassAnnotationMatchProcessor processor);

  /**
   * @param processor
   */
  ISlizaaPluginRegistry registerMethodAnnotationMatchProcessor(IMethodAnnotationMatchProcessor processor);

  /**
   * @param type
   * @param codeSource
   */
  <T> ISlizaaPluginRegistry registerCodeSourceToScan(Class<T> type, T codeSource);

  /**
   * @param type
   * @param codeSource
   */
  <T> ISlizaaPluginRegistry unregisterCodeSourceToScan(Class<T> type, T codeSource);

  /**
   * @param type
   * @param classLoaderProvider
   */
  <T> ISlizaaPluginRegistry registerCodeSourceClassLoaderProvider(Class<T> type, Function<T, ClassLoader> classLoaderProvider);

  /**
   * @param type
   * @param classLoaderProvider
   */
  <T> ISlizaaPluginRegistry unregisterCodeSourceClassLoaderProvider(Class<T> type);

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
