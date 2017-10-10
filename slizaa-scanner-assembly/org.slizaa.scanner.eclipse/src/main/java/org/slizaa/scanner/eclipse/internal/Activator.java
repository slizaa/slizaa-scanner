package org.slizaa.scanner.eclipse.internal;

import java.util.Collections;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.impl.graphdbfactory.GraphDbFactory;
import org.slizaa.scanner.core.impl.importer.ModelImporterFactory;
import org.slizaa.scanner.core.impl.plugins.DefaultClassAnnotationMatchProcessor;
import org.slizaa.scanner.core.impl.plugins.SlizaaPluginRegistry;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;
import org.slizaa.scanner.core.spi.parser.IParserFactory;

/**
 */
public class Activator implements BundleActivator {

  /** - */
  private BundleContext                                        _bundleContext;

  /** - */
  private SlizaaScannerExtensionBundleTracker                  _tracker;

  /** - */
  private SlizaaPluginRegistry                                 _pluginRegistry;

  /** - */
  private DefaultClassAnnotationMatchProcessor<IParserFactory> _parserFactoryCollector;

  @Override
  public void start(BundleContext context) throws Exception {

    //
    _bundleContext = context;

    //
    _pluginRegistry = new SlizaaPluginRegistry().registerCodeSourceClassLoaderProvider(Bundle.class,
        bundle -> bundle.adapt(BundleWiring.class).getClassLoader());

    //
    _parserFactoryCollector = _pluginRegistry.registerClassAnnotationMatchProcessor(
        new DefaultClassAnnotationMatchProcessor<>(SlizaaParserFactory.class, cl -> (IParserFactory) cl.newInstance()));

    //
    _tracker = new SlizaaScannerExtensionBundleTracker(_bundleContext, _pluginRegistry);
    _tracker.open();

    //
    context.registerService(IModelImporterFactory.class.getName(),
        new ModelImporterFactory(() -> _parserFactoryCollector.getCollectedClasses().toArray(new IParserFactory[0])),
        null);

    //
    context.registerService(IGraphDbFactory.class.getName(), new GraphDbFactory(() -> {
      // SlizaaPluginRegistry pluginRegistry = new SlizaaPluginRegistry(getExtensionClassLoader());
      // pluginRegistry.initialize();
      // return pluginRegistry.getNeo4jExtensions();
      return Collections.emptyList();
    }), null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop(BundleContext context) throws Exception {

    _tracker.close();
    _bundleContext = null;
  }
}
