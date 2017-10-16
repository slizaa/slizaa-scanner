package org.slizaa.scanner.eclipse.itest;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;

/**
 * <p>
 * </p>
 */
public class SlizaaScannerExtensionBundleTracker extends BundleTracker<Bundle> {

  /**
   * <p>
   * </p>
   * 
   * @param context
   * @param stateMask
   * @param customizer
   */
  public SlizaaScannerExtensionBundleTracker(BundleContext context) {
    super(context, Bundle.RESOLVED | Bundle.STARTING | Bundle.ACTIVE | Bundle.STOPPING, null);
  }

  @Override
  public Bundle addingBundle(Bundle bundle, BundleEvent event) {

    //
    String header = bundle.getHeaders().get("Slizaa-Extension");
    if (header != null && header.equals("true")) {
      return bundle;
    }

    //
    return null;
  }
}