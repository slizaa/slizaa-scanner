package org.slizaa.scanner.assembly.itest.eclipse;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class CheckResolveTest extends AbstractEclipseTest {

  /**
   * <p>
   * </p>
   * 
   * @throws BundleException
   */
  @Test
  public void testResolve() throws BundleException {

    //
    for (Bundle bundle : bundleContext().getBundles()) {
      bundle.start();
    }
  }
}