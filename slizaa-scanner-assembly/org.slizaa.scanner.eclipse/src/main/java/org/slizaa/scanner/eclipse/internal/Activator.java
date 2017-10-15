package org.slizaa.scanner.eclipse.internal;

import java.util.Arrays;
import java.util.Collections;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.impl.graphdbfactory.GraphDbFactory;
import org.slizaa.scanner.core.impl.importer.ModelImporterFactory;
import org.slizaa.scanner.core.impl.plugins.ClasspathScannerFactory;
import org.slizaa.scanner.core.impl.plugins.DefaultClassAnnotationHandler;
import org.slizaa.scanner.core.impl.plugins.ScannerProcessors;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;
import org.slizaa.scanner.core.spi.parser.IParserFactory;

/**
 */
public class Activator implements BundleActivator {

  /** - */
  private BundleContext           _bundleContext;

  /** - */
  private ClasspathScannerFactory _scannerFactory;

  @Override
  public void start(BundleContext context) throws Exception {

    //
    _bundleContext = context;

    //
    _scannerFactory = new ClasspathScannerFactory().registerCodeSourceClassLoaderProvider(Bundle.class,
        bundle -> bundle.adapt(BundleWiring.class).getClassLoader());

    //
    _scannerFactory.scan(Arrays.asList(_tracker.getBundles()), processors);

    //
    context.registerService(IModelImporterFactory.class.getName(), new ModelImporterFactory(), null);

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
    _bundleContext = null;
  }
}
