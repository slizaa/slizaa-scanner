package org.slizaa.scanner.core.impl.plugins;

import java.util.function.Function;

/**
 * <p>
 * </p>
 */
public interface ISlizaaPluginRegistry {

  /**
   * @param processor
   */
  void registerClassAnnotationMatchProcessor(IClassAnnotationMatchProcessor processor);

  /**
   * @param processor
   */
  void registerMethodAnnotationMatchProcessor(IMethodAnnotationMatchProcessor processor);

  /**
   * @param type
   * @param codeSource
   */
  <T> void registerCodeSourceToScan(Class<T> type, T codeSource);

  /**
   * @param type
   * @param codeSource
   */
  <T> void unregisterCodeSourceToScan(Class<T> type, T codeSource);

  /**
   * @param type
   * @param classLoaderProvider
   */
  <T> void registerCodeSourceClassLoaderProvider(Class<T> type, Function<T, ClassLoader> classLoaderProvider);

  /**
   * @param type
   * @param classLoaderProvider
   */
  <T> void unregisterCodeSourceClassLoaderProvider(Class<T> type);

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
