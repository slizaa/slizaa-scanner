package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultClassAnnotationMatchProcessor<T> implements IClassAnnotationMatchProcessor {

  /** - */
  private Class<? extends Annotation>         _annotationToMatch;

  /** - */
  private Map<Object, Map<Class<?>, T>>       _collectedClasses;

  /** - */
  private TransformationFunction<Class<?>, T> _transformationFunction;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultClassAnnotationMatchProcessor}.
   * </p>
   *
   * @param annotationToMatch
   */
  public DefaultClassAnnotationMatchProcessor(Class<? extends Annotation> annotationToMatch,
      TransformationFunction<Class<?>, T> transformationFunction) {

    //
    _annotationToMatch = checkNotNull(annotationToMatch);
    _transformationFunction = checkNotNull(transformationFunction);

    //
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
    _collectedClasses.computeIfAbsent(codeSource, key -> new HashMap<>()).clear();
  }

  @Override
  public void changed(Object codeSource, List<Class<?>> classesWithAnnotation) {

    //
    Map<Class<?>, T> values = _collectedClasses.get(codeSource);

    //
    List<Class<?>> removedValues = new ArrayList<Class<?>>(values.keySet());
    removedValues.removeAll(classesWithAnnotation);
    removedValues.forEach(removedKey -> values.remove(removedKey));

    //
    classesWithAnnotation.forEach(key -> {
      if (!values.containsKey(key)) {
        try {
          T value = _transformationFunction.apply(key);
          values.put(key, value);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  public void removed(Object codeSource) {
    _collectedClasses.remove(codeSource);
  }

  public List<T> getCollectedClasses() {
    return _collectedClasses.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
  }
}
