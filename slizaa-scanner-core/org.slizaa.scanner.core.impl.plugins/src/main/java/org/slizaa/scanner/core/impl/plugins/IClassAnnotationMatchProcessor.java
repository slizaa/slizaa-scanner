package org.slizaa.scanner.core.impl.plugins;

import java.lang.annotation.Annotation;

public interface IClassAnnotationMatchProcessor {

  Class<? extends Annotation> getAnnotationToMatch();

  void scanStart(Object object);
  
  void consume(Class<?> classWithAnnotation);
  
  void scanStop(Object object);
}
