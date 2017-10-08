package org.slizaa.scanner.core.impl.plugins;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.jar.Manifest;

import org.neo4j.kernel.impl.util.CopyOnWriteHashMap;

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
    ClassAnnotationMatchProcessorAdapter adapter = new ClassAnnotationMatchProcessorAdapter(processor);

    //
    if (!_classAnnotationMatchProcessors.contains(checkNotNull(adapter))) {
      _classAnnotationMatchProcessors.add(adapter);
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
    _codeSourceToScan.get(checkNotNull(type)).forEach(codeSource -> scanSingleElement(type, (T) codeSource));
  }

  @Override
  public <T> void scan(Class<T> type, T codeSource) {
    scanSingleElement(checkNotNull(type), checkNotNull(codeSource));
  }

  /**
   * <p>
   * </p>
   *
   * @param classLoaders
   */
  private <T> void scanSingleElement(Class<T> type, T codeSource) {

    //
    ClassLoader classLoader = classLoader(checkNotNull(type), checkNotNull(codeSource));

    //
    FastClasspathScanner classpathScanner = new FastClasspathScanner()

        // ignore parent class loaders
        .ignoreParentClassLoaders(true)

        // set the class loader to scan
        .overrideClassLoaders(classLoader);

    //
    _classAnnotationMatchProcessors.forEach(adapter -> {

      classpathScanner.matchClassesWithAnnotation(adapter.getAnnotationToMatch(), classWithAnnotation -> {
        adapter.addClassWithAnnotation(classWithAnnotation);
      });
    });

    //
    _classAnnotationMatchProcessors.forEach(adapter -> adapter.beforeScan());

    // Actually perform the scan (nothing will happen without this call)
    classpathScanner.scan();

    //
    _classAnnotationMatchProcessors.forEach(adapter -> adapter.afterScan(codeSource, classLoader));
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
