package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultClassAnnotationMatchProcessor implements IClassAnnotationMatchProcessor {

  /** - */
  private Class<? extends Annotation>                _annotationToMatch;

  /** - */
  private Map<Class<?>, Map<Object, List<Class<?>>>> _collectedClasses;

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

  /**
   * {@inheritDoc}
   */
  @Override
  public void consume(Object codeSource, Class<?> codeSourceType, List<Class<?>> classesWithAnnotation) {

    //
    Map<Object, List<Class<?>>> coudeSourceMap = _collectedClasses.computeIfAbsent(codeSourceType,
        key -> new HashMap<>());

    //
    coudeSourceMap.put(codeSource, classesWithAnnotation);
  }

  public List<Class<?>> getCollectedClasses() {
    return _collectedClasses.values().stream().flatMap(map -> map.values().stream()).flatMap(l -> l.stream()).collect(Collectors.toList());
  }
}
