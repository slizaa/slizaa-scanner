package org.slizaa.scanner.core.impl.plugins;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IMethodAnnotationMatchProcessor {

  Class<? extends Annotation> getAnnotationToMatch();

  void consume(Map<Class<?>, List<Executable>> matchingMethodsOrConstructors);
}
