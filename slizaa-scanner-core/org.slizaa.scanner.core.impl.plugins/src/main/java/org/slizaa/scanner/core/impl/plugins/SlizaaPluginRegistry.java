package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

// TODO: consistence checks!
public class SlizaaPluginRegistry implements ISlizaaPluginRegistry {

  /** - */
  private List<ClassAnnotationMatchProcessorAdapter> _classAnnotationMatchProcessors;

  /** - */
  private List<IMethodAnnotationMatchProcessor>      _methodAnnotationMatchProcessors;

  /** - */
  private Map<Class<?>, List<?>>                     _codeSourceToScan;

  /** - */
  private Map<Class<?>, Function<?, ClassLoader>>    _classloaderProvider;

  /** - */
  private Object                                     _lock = new Object();

  /**
   * 
   */
  public SlizaaPluginRegistry() {

    //
    _classAnnotationMatchProcessors = new ArrayList<>();
    _methodAnnotationMatchProcessors = new ArrayList<>();
    _codeSourceToScan = new HashMap<>();
    _classloaderProvider = new HashMap<>();
  }

  /**
   * <p>
   * </p>
   *
   * @param type
   * @param classLoaderProvider
   * @return
   */
  public <T> SlizaaPluginRegistry registerCodeSourceClassLoaderProvider(Class<T> type,
      Function<T, ClassLoader> classLoaderProvider) {

    checkNotNull(type);
    checkNotNull(classLoaderProvider);

    synchronized (_lock) {
      _classloaderProvider.put(type, classLoaderProvider);
    }

    //
    return this;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends IClassAnnotationMatchProcessor> T registerClassAnnotationMatchProcessor(T processor) {

    //
    checkNotNull(processor);

    //
    ClassAnnotationMatchProcessorAdapter adapter = new ClassAnnotationMatchProcessorAdapter(processor);

    //
    synchronized (_lock) {
      if (!_classAnnotationMatchProcessors.contains(adapter)) {

        // add...
        _classAnnotationMatchProcessors.add(adapter);

        // ... and scan
        _codeSourceToScan.forEach((type, codeSourceList) -> {
          codeSourceList.forEach(codeSource -> {
            scanSingleElement((Class) type, codeSource, Collections.singletonList(adapter));
          });
        });
      }
    }

    //
    return processor;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends IMethodAnnotationMatchProcessor> T registerMethodAnnotationMatchProcessor(T processor) {

    throw new UnsupportedOperationException();
//    checkNotNull(processor);
//
//    //
//    if (!_methodAnnotationMatchProcessors.contains(processor)) {
//      _methodAnnotationMatchProcessors.add(processor);
//    }
//
//    //
//    return this;
  }

  /**
   * <p>
   * </p>
   *
   * @param type
   * @param codeSource
   * @return
   */
  public <T> void registerCodeSourceToScan(Class<T> type, T codeSource) {

    checkNotNull(type);
    checkNotNull(codeSource);

    // TODO: check class loader provider

    //
    @SuppressWarnings("unchecked")
    List<T> list = (List<T>) _codeSourceToScan.computeIfAbsent(type, t -> new ArrayList<>());
    if (!list.contains(codeSource)) {
      list.add(codeSource);
    }

    //
    _classAnnotationMatchProcessors.forEach(adapter -> {
      adapter.added(codeSource);
    });

    //
    scanSingleElement(type, codeSource, _classAnnotationMatchProcessors);
  }

  /**
   * <p>
   * </p>
   *
   * @param type
   * @param codeSource
   * @return
   */
  public <T> void unregisterCodeSourceToScan(Class<T> type, T codeSource) {

    //
    @SuppressWarnings("unchecked")
    List<T> list = (List<T>) _codeSourceToScan.get(type);
    if (list != null) {
      list.remove(codeSource);
    }

    //
    _classAnnotationMatchProcessors.forEach(adapter -> {
      adapter.removed(codeSource);
    });
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> void rescan(Class<T> type) {
    _codeSourceToScan.get(checkNotNull(type))
        .forEach(codeSource -> scanSingleElement(type, (T) codeSource, _classAnnotationMatchProcessors));
  }

  @Override
  public <T> void rescan(Class<T> type, T codeSource) {
    scanSingleElement(checkNotNull(type), checkNotNull(codeSource), _classAnnotationMatchProcessors);
  }

  /**
   * <p>
   * </p>
   *
   * @param classLoaders
   */
  private <T> void scanSingleElement(Class<T> type, T codeSource,
      List<ClassAnnotationMatchProcessorAdapter> classAnnotationAdapters) {

    //
    ClassLoader classLoader = classLoader(checkNotNull(type), checkNotNull(codeSource));

    //
    FastClasspathScanner classpathScanner = new FastClasspathScanner()

        // ignore parent class loaders
        .ignoreParentClassLoaders(true)

        // set the class loader to scan
        .overrideClassLoaders(classLoader);

    //
    classAnnotationAdapters.forEach(adapter -> {
      classpathScanner.matchClassesWithAnnotation(adapter.getAnnotationToMatch(), classWithAnnotation -> {
        adapter.addClassWithAnnotation(classWithAnnotation);
      });
    });

    //
    classAnnotationAdapters.forEach(adapter -> adapter.beforeScan());

    // Actually perform the scan (nothing will happen without this call)
    classpathScanner.scan();

    //
    classAnnotationAdapters.forEach(adapter -> adapter.afterScan(codeSource, type, classLoader));
  }

  /**
   * <p>
   * </p>
   *
   * @param type
   * @param codeSource
   * @return
   */
  private <T> ClassLoader classLoader(Class<T> type, T codeSource) {

    //
    @SuppressWarnings("unchecked")
    Function<T, ClassLoader> classloaderProvider = (Function<T, ClassLoader>) _classloaderProvider
        .get(checkNotNull(type));

    //
    return classloaderProvider.apply(codeSource);
  }
}
