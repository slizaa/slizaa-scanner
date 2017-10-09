package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

public class ClassAnnotationMatchProcessorAdapter {

  /** - */
  private IClassAnnotationMatchProcessor _processor;

  /** - */
  private List<Class<?>>                 _result;

  /**
   * <p>
   * Creates a new instance of type {@link ClassAnnotationMatchProcessorAdapter}.
   * </p>
   *
   * @param processor
   */
  public ClassAnnotationMatchProcessorAdapter(IClassAnnotationMatchProcessor processor) {

    //
    checkNotNull(processor);
    checkNotNull(processor.getAnnotationToMatch(), "Method getAnnotationToMatch() must not return null.");

    //
    _processor = processor;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public Class<? extends Annotation> getAnnotationToMatch() {
    return _processor.getAnnotationToMatch();
  }

  /**
   * <p>
   * </p>
   *
   */
  public void beforeScan() {
    _result = new LinkedList<>();
  }

  /**
   * <p>
   * </p>
   *
   * @param classWithAnnotation
   */
  public void addClassWithAnnotation(Class<?> classWithAnnotation) {
    _result.add(classWithAnnotation);
  }

  /**
   * <p>
   * </p>
   *
   * @param codeSource
   * @param classLoader
   */
  public void afterScan(Object codeSource, Class<?> type, ClassLoader classLoader) {
    _processor.consume(codeSource, type, _result);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_processor == null) ? 0 : _processor.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClassAnnotationMatchProcessorAdapter other = (ClassAnnotationMatchProcessorAdapter) obj;
    if (_processor == null) {
      if (other._processor != null)
        return false;
    } else if (!_processor.equals(other._processor))
      return false;
    return true;
  }
}
