package org.slizaa.scanner.core.impl.plugins;

import java.util.List;

import org.slizaa.scanner.core.spi.parser.IParserFactory;

/**
 */
public interface ISlizaaPluginRegistry {

  void registerClassAnnotationMatchProcessor(IClassAnnotationMatchProcessor processor);

  void registerMethodAnnotationMatchProcessor(IMethodAnnotationMatchProcessor processor);

  <T> void registerCodeSourceToScan(Class<T> type, T codeSource);

  <T> void unregisterCodeSourceToScan(Class<T> type, T codeSource);

  void retriggerScan(Class<?> type);

  List<Class<?>> getNeo4jExtensions();

  List<Class<? extends IParserFactory>> getParserFactories();
}
