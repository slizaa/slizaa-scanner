package org.slizaa.scanner.core.impl.plugins;

@FunctionalInterface
public interface TransformationFunction<T, R> {

  /**
   * Applies this function to the given argument.
   *
   * @param t
   *          the function argument
   * @return the function result
   */
  R apply(T t) throws Exception;

}