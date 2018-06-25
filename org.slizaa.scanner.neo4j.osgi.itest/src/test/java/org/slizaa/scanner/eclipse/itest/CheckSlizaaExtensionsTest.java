/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.eclipse.itest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.util.tracker.BundleTracker;
import org.slizaa.core.classpathscanner.ClasspathScannerFactoryBuilder;
import org.slizaa.core.classpathscanner.IClasspathScannerFactory;
import org.slizaa.scanner.core.spi.annotations.ParserFactory;

public class CheckSlizaaExtensionsTest extends AbstractEclipseTest {

  /**
   * <p>
   * </p>
   *
   * @throws BundleException
   */
  @Test
  public void testBundleAndServices() throws BundleException {

    // checkStart
    startAllBundles();

    //
    SlizaaScannerExtensionBundleTracker tracker = new SlizaaScannerExtensionBundleTracker(bundleContext());
    tracker.open();

    //
    IClasspathScannerFactory scannerFactory = ClasspathScannerFactoryBuilder.newClasspathScannerFactory()
        .registerCodeSourceClassLoaderProvider(Bundle.class,
            bundle -> bundle.adapt(BundleWiring.class).getClassLoader())
        .create();

    //
    Map<String, List<Class<?>>> scanResult = new HashMap<>();

    // scan
    scannerFactory.createScanner((Object[]) tracker.getBundles()).matchClassesWithAnnotation(ParserFactory.class,
        (source, pf) -> scanResult.put(((Bundle) source).getSymbolicName(), pf)).scan();

    //
    assertThat(scanResult).hasSize(1);
    assertThat(scanResult.get("org.slizaa.scanner.core.contentdefinition")).isNotNull().isEmpty();
  }

  /**
   */
  public static class SlizaaScannerExtensionBundleTracker extends BundleTracker<Bundle> {

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
}
