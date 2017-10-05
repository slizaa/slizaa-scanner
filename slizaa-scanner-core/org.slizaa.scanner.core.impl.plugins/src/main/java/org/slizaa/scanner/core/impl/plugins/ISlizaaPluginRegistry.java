package org.slizaa.scanner.core.impl.plugins;

import java.util.function.Function;

/**
 */
public interface ISlizaaPluginRegistry {

  void registerClassAnnotationMatchProcessor(IClassAnnotationMatchProcessor processor);

  void registerMethodAnnotationMatchProcessor(IMethodAnnotationMatchProcessor processor);

  <T> void registerCodeSourceToScan(Class<T> type, T codeSource);

  <T> void unregisterCodeSourceToScan(Class<T> type, T codeSource);

  <T> void registerCodeSourceClassLoaderProvider(Class<T> type, Function<?, ClassLoader> classLoaderProvider);

  <T> void unregisterCodeSourceClassLoaderProvider(Class<T> type, Function<?, ClassLoader> classLoaderProvider);

  <T> void rescan(Class<T> type);
}
