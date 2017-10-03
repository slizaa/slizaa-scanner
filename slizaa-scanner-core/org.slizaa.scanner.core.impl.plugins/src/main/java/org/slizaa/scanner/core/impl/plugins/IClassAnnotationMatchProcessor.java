package org.slizaa.scanner.core.impl.plugins;

public interface IClassAnnotationMatchProcessor {

  Class<?> getAnnotationToMatch();

  void scanStart();
  
  void consume(Class<?> classWithAnnotation);
  
  void scanStop();
}
