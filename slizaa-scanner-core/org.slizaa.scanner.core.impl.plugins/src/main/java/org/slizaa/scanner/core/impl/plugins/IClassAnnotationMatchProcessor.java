package org.slizaa.scanner.core.impl.plugins;

public interface IClassAnnotationMatchProcessor {

  Class<?> getAnnotationToMatch();

  void scanStart(Object object);
  
  void consume(Class<?> classWithAnnotation);
  
  void scanStop(Object object);
}
