package org.slizaa.scanner.core.impl.plugins;

import java.lang.reflect.Executable;

public interface IMethodAnnotationMatchProcessor {

  Class<?> getAnnotationToMatch();

  void scanStart(Object object);

  void consume(Class<?> matchingClass, Executable matchingMethodOrConstructor);

  void scanStop(Object object);
}
