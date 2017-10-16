package org.slizaa.scanner.eclipse.itest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.osgi.framework.BundleException;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;

public class CheckBundlesAndServicesTest extends AbstractEclipseTest {

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

    // checkServices
    assertThat(bundleContext().getServiceReference(IModelImporterFactory.class)).isNotNull();
    assertThat(bundleContext().getServiceReference(IGraphDbFactory.class)).isNotNull();
  }
}