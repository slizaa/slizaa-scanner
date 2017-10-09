package org.slizaa.scanner.eclipse.internal;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

public class Activator implements BundleActivator {

  /** - */
  private BundleContext                        _bundleContext;

  /** - */
  private SlizaaScannerExtensionBundleTracker  _tracker;

  /** - */
  private SlizaaPluginRegistry                 _pluginRegistry;

  /** - */
  private DefaultClassAnnotationMatchProcessor _parserFactoryCollector;

  @Override
  public void start(BundleContext context) throws Exception {

    //
    _bundleContext = context;

    //
    _parserFactoryCollector = new DefaultClassAnnotationMatchProcessor(SlizaaParserFactory.class);

    //
    _pluginRegistry = new SlizaaPluginRegistry()

        //
        .registerCodeSourceClassLoaderProvider(Bundle.class,
            bundle -> bundle.adapt(BundleWiring.class).getClassLoader())

        //
        .registerClassAnnotationMatchProcessor(_parserFactoryCollector);

    //
    _tracker = new SlizaaScannerExtensionBundleTracker(_bundleContext, _pluginRegistry);
    _tracker.open();

    //
    context.registerService(IModelImporterFactory.class.getName(),
        new ModelImporterFactory(() -> createParserFactories()), null);

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

  /**
   * @return
   */
  private IParserFactory[] createParserFactories() {

    //
    _pluginRegistry.scan();

    // TODO CACHE!!
    List<IParserFactory> result = new LinkedList<>();
    _parserFactoryCollector.getCollectedResult().stream().filter(cl -> IParserFactory.class.isAssignableFrom(cl))
        .forEach(cl -> {

          try {
            result.add((IParserFactory) cl.newInstance());
          } catch (Exception e) {
            // TODO: handle exception
          }

        });

    //
    return result.toArray(new IParserFactory[0]);
  }
}
