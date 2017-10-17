package org.slizaa.scanner.jtype.eclipse.itest;

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
import org.slizaa.scanner.core.classpathscanner.ClasspathScannerFactoryBuilder;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerFactory;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;
import org.slizaa.scanner.jtype.bytecode.JTypeByteCodeParserFactory;

public class CheckSlizaaExtensionsTest extends AbstractJTypeTest {

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
    scannerFactory.createScanner((Object[]) tracker.getBundles()).matchClassesWithAnnotation(SlizaaParserFactory.class,
        (source, pf) -> scanResult.put(((Bundle) source).getSymbolicName(), pf)).scan();

    //
    assertThat(scanResult).hasSize(2);
    assertThat(scanResult.get("org.slizaa.scanner.core.contentdefinition")).isNotNull().isEmpty();
    assertThat(scanResult.get("org.slizaa.scanner.jtype")).isNotNull()
        .containsExactly(JTypeByteCodeParserFactory.class);
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