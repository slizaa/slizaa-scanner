package org.slizaa.scanner.eclipse.internal;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.slizaa.scanner.core.impl.plugins.SlizaaPluginRegistry;

/**
 * <p>
 * </p>
 */
public class SlizaaScannerExtensionBundleTracker extends BundleTracker<Bundle> {

  /** - */
  private SlizaaPluginRegistry _pluginRegistry;

  /**
   * <p>
   * </p>
   * 
   * @param context
   * @param stateMask
   * @param customizer
   */
  public SlizaaScannerExtensionBundleTracker(BundleContext context, SlizaaPluginRegistry pluginRegistry) {
    super(context, Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING, null);

    //
    _pluginRegistry = pluginRegistry;
  }

  @Override
  public Bundle addingBundle(Bundle bundle, BundleEvent event) {

    //
    String header = bundle.getHeaders().get("Slizaa-Extension");
    if (header != null && header.equals("true")) {
      _pluginRegistry.registerCodeSourceToScan(Bundle.class, bundle);
      return bundle;
    }

    //
    return null;
  }

  @Override
  public void removedBundle(Bundle bundle, BundleEvent event, Bundle object) {
    super.removedBundle(bundle, event, object);
    _pluginRegistry.unregisterCodeSourceToScan(Bundle.class, bundle);
  }
}