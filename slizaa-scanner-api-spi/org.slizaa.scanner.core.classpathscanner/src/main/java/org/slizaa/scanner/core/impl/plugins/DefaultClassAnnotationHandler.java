package org.slizaa.scanner.core.impl.plugins;

import static org.slizaa.scanner.core.spi.internal.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 *
 * @param <T>
 */
public class DefaultClassAnnotationHandler<T> implements IClassAnnotationMatchProcessor {

  /** - */
  private Map<Object, Map<Class<?>, T>> _collectedClasses;

  /** - */
  private Function<Class<?>, T>         _transformationFunction;

  /**
   * <p>
   * Creates a new instance of type {@link DefaultClassAnnotationHandler}.
   * </p>
   *
   * @param annotationToMatch
   */
  public DefaultClassAnnotationHandler(Function<Class<?>, T> transformationFunction) {

    //
    _transformationFunction = checkNotNull(transformationFunction);

    //
    _collectedClasses = new HashMap<>();
  }

  @Override
  public void processMatch(Object codeSource, List<Class<?>> classesWithAnnotation) {

    //
    Map<Class<?>, T> values = _collectedClasses.computeIfAbsent(codeSource, key -> new HashMap<>());

    //
    List<Class<?>> removedValues = new ArrayList<Class<?>>(values.keySet());
    removedValues.removeAll(classesWithAnnotation);
    removedValues.forEach(removedKey -> values.remove(removedKey));

    //
    classesWithAnnotation.forEach(key -> {
      if (!values.containsKey(key)) {
          T value = _transformationFunction.apply(key);
          if (value != null) {
            values.put(key, value);
          }
      }
    });
  }

  public List<T> getResult() {
    return _collectedClasses.values().stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
  }
}
