package org.slizaa.scanner.core.eclipse.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.BundleTracker;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.impl.graphdbfactory.GraphDbFactory;
import org.slizaa.scanner.core.impl.plugins.SlizaaPluginRegistry;
import org.slizaa.scanner.importer.ModelImporterFactory;

public class Activator implements BundleActivator {

  private BundleContext         _bundleContext;

  private BundleTracker<Bundle> _tracker;

  @Override
  public void start(BundleContext context) throws Exception {

    //
    _bundleContext = context;

    //
    _tracker = new BundleTracker<Bundle>(_bundleContext,
        Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING, null) {

      @Override
      public Bundle addingBundle(Bundle bundle, BundleEvent event) {

        //
        String header = bundle.getHeaders().get("Slizaa-Scanner-Extension");
        if (header != null && header.equals("true")) {
          return bundle;
        }

        //
        return null;
      }
    };
    _tracker.open();

    //
    context.registerService(IModelImporterFactory.class.getName(), new ModelImporterFactory(), null);
    context.registerService(IGraphDbFactory.class.getName(), new GraphDbFactory(() -> {
      SlizaaPluginRegistry pluginRegistry = new SlizaaPluginRegistry(getExtensionClassLoader());
      pluginRegistry.initialize();
      return pluginRegistry.getNeo4jExtensions();
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
   * <p>
   * </p>
   *
   * @return
   */
  private List<ClassLoader> getExtensionClassLoader() {

    Bundle[] bundles = _tracker.getBundles();

    //
    if (bundles != null) {
      return Arrays.stream(bundles).map(bundle -> bundle.adapt(BundleWiring.class).getClassLoader())
          .collect(Collectors.toList());
    }
    //
    else {
      return Collections.emptyList();
    }
  }
}
