package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultClassAnnotationMatchProcessor implements IClassAnnotationMatchProcessor {

  /** - */
  private Class<? extends Annotation> _annotationToMatch;

  /** - */
  private Map<Object, List<Class<?>>> _collectedClasses;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultClassAnnotationMatchProcessor}.
   * </p>
   *
   * @param annotationToMatch
   */
  public DefaultClassAnnotationMatchProcessor(Class<? extends Annotation> annotationToMatch) {
    _annotationToMatch = checkNotNull(annotationToMatch);
    _collectedClasses = new HashMap<>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends Annotation> getAnnotationToMatch() {
    return _annotationToMatch;
  }

  @Override
  public void added(Object codeSource) {
    _collectedClasses.computeIfAbsent(codeSource, key -> new ArrayList<>());
  }

  @Override
  public void changed(Object codeSource, List<Class<?>> classesWithAnnotation) {

    //
    _collectedClasses.put(codeSource, classesWithAnnotation);
  }

  @Override
  public void removed(Object codeSource) {
    _collectedClasses.remove(codeSource);
  }

  public List<Class<?>> getCollectedClasses() {
    return _collectedClasses.values().stream().flatMap(l -> l.stream()).collect(Collectors.toList());
  }
}
