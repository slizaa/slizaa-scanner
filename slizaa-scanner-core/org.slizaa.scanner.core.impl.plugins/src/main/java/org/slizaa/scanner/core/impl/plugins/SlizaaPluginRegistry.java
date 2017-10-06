package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.neo4j.kernel.impl.util.CopyOnWriteHashMap;

import com.google.common.base.Stopwatch;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import shaded.org.apache.commons.lang.time.StopWatch;

public class SlizaaPluginRegistry implements ISlizaaPluginRegistry {

  /** - */
  private List<IClassAnnotationMatchProcessor>    _classAnnotationMatchProcessors;

  /** - */
  private List<IMethodAnnotationMatchProcessor>   _methodAnnotationMatchProcessors;

  /** - */
  private Map<Class<?>, List<?>>                  _codeSourceToScan;

  /** - */
  private Map<Class<?>, Function<?, ClassLoader>> _classloaderProvider;

  /**
   * 
   */
  public SlizaaPluginRegistry() {

    //
    _classAnnotationMatchProcessors = new CopyOnWriteArrayList<>();
    _methodAnnotationMatchProcessors = new CopyOnWriteArrayList<>();
    _codeSourceToScan = new CopyOnWriteHashMap<>();
    _classloaderProvider = new CopyOnWriteHashMap<>();
  }

  @Override
  public void registerClassAnnotationMatchProcessor(IClassAnnotationMatchProcessor processor) {

    //
    if (!_classAnnotationMatchProcessors.contains(checkNotNull(processor))) {
      _classAnnotationMatchProcessors.add(processor);
    }
  }

  @Override
  public void registerMethodAnnotationMatchProcessor(IMethodAnnotationMatchProcessor processor) {

    //
    if (!_methodAnnotationMatchProcessors.contains(checkNotNull(processor))) {
      _methodAnnotationMatchProcessors.add(processor);
    }
  }

  @Override
  public <T> void registerCodeSourceToScan(Class<T> type, T codeSource) {

    //
    @SuppressWarnings("unchecked")
    List<T> list = (List<T>) _codeSourceToScan.computeIfAbsent(type, t -> new CopyOnWriteArrayList<>());
    if (!list.contains(codeSource)) {
      list.add(codeSource);
    }
  }

  @Override
  public <T> void unregisterCodeSourceToScan(Class<T> type, T codeSource) {

    //
    @SuppressWarnings("unchecked")
    List<T> list = (List<T>) _codeSourceToScan.get(type);
    if (list != null) {
      list.remove(codeSource);
    }
  }

  @Override
  public <T> void registerCodeSourceClassLoaderProvider(Class<T> type, Function<T, ClassLoader> classLoaderProvider) {
    _classloaderProvider.put(checkNotNull(type), checkNotNull(classLoaderProvider));

  }

  @Override
  public <T> void unregisterCodeSourceClassLoaderProvider(Class<T> type) {
    _classloaderProvider.remove(checkNotNull(type));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> void scan(Class<T> type) {

    Stopwatch stopwatch = Stopwatch.createStarted();

    //
    Function<T, ClassLoader> classloaderProvider = (Function<T, ClassLoader>) _classloaderProvider
        .get(checkNotNull(type));

    List<ClassLoader> classLoaders = _codeSourceToScan.get(checkNotNull(type)).stream()
        .map(e -> classloaderProvider.apply((T) e)).collect(Collectors.toList());

    //
    FastClasspathScanner classpathScanner = new FastClasspathScanner()

        // set the class loader to scan
        .overrideClassLoaders(classLoaders.toArray(new ClassLoader[0]));

    //
    _classAnnotationMatchProcessors.forEach(processor -> {
      System.out.println("Adding processor: " + processor);
      checkNotNull(processor.getAnnotationToMatch(), "Method getAnnotationToMatch() must not return null.");

      classpathScanner.matchClassesWithAnnotation(processor.getAnnotationToMatch(), classWithAnnotation -> {
        System.out.println("classWithAnnotation" + classWithAnnotation.getName());
        processor.consume(classWithAnnotation);
      });
    });

    System.out.println("1: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    
    //
    // _methodAnnotationMatchProcessors.forEach(processor -> {
    //
    // checkNotNull(processor.getAnnotationToMatch(), "Method getAnnotationToMatch() must not return null.");
    //
    // classpathScanner.matchClassesWithMethodAnnotation(processor.getAnnotationToMatch(),
    // (classWithAnnotation, method) -> processor.consume(classWithAnnotation, method));
    // });

    // Actually perform the scan (nothing will happen without this call)
    classpathScanner.scan();
    
    System.out.println("2: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  @Override
  public <T> void scan(Class<T> type, T codeSource) {
    // TODO Auto-generated method stub

  }
}
