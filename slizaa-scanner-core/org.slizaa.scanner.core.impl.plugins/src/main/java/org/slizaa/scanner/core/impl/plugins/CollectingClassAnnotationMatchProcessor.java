package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectingClassAnnotationMatchProcessor implements IClassAnnotationMatchProcessor {

  /** - */
  private Class<? extends Annotation> _annotationToMatch;

  /** - */
  private Map<Object, List<Class<?>>> _collectedClasses;

  /**
   */
  public CollectingClassAnnotationMatchProcessor(Class<? extends Annotation> annotationToMatch) {
    _annotationToMatch = checkNotNull(annotationToMatch);
    _collectedClasses = new HashMap<Object, List<Class<?>>>();
  }

  @Override
  public Class<? extends Annotation> getAnnotationToMatch() {
    return _annotationToMatch;
  }

  @Override
  public void consume(Object codeSource, List<Class<?>> classesWithAnnotation, Class<?> codeSourceType,
      ClassLoader classLoader) {

    //
    _collectedClasses.put(codeSource, classesWithAnnotation);
  }

  /**
   * 
   */
  public void clear() {
    _collectedClasses.clear();
  }

  public List<Class<?>> getCollectedClasses() {
    return _collectedClasses.values().stream().flatMap(l -> l.stream()).collect(Collectors.toList());
  }
}
