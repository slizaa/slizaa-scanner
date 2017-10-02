package org.slizaa.scanner.eclipse.itest;

import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackages;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.MavenUtils;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.ops4j.pax.url.mvn.MavenResolvers;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public abstract class AbstractEclipseTest {

  {
    System.setProperty("org.ops4j.pax.url.mvn.localRepository",
        System.getProperty("user.home") + File.separator + ".m2" + File.separator + "repository");
  }

  /** - */
  @Inject
  private BundleContext bundleContext;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public BundleContext bundleContext() {
    return bundleContext;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   * @throws IOException
   */
  @Configuration
  public Option[] config() throws IOException {

    // resolve jtype version
    String jtypeVersion = MavenUtils.getArtifactVersion("org.slizaa.scanner.jtype", "org.slizaa.scanner.jtype");

    // resolve the jtype artifact
    File jtypeFile = MavenResolvers.createMavenResolver(null, null).resolve("org.slizaa.scanner.jtype",
        "org.slizaa.scanner.jtype", null, null, jtypeVersion);

    //
    return options(

        //
        bootDelegationPackages("sun.*", "com.sun.*"),

        // add the test dependencies
        mavenBundle("org.assertj", "assertj-core").versionAsInProject(), junitBundles(),

        //
        mavenBundle("com.google.guava", "guava").versionAsInProject(),

        mavenBundle("org.eclipse.platform", "org.eclipse.equinox.common").versionAsInProject(),
        mavenBundle("com.google.code.gson", "gson").versionAsInProject(),
        mavenBundle("org.ops4j.pax.url", "pax-url-aether").versionAsInProject(),
        mavenBundle("io.github.lukehutch", "fast-classpath-scanner").versionAsInProject(),

        //
        wrappedBundle(mavenBundle("org.neo4j.driver", "neo4j-java-driver").versionAsInProject()),

        //
        mavenBundle("org.slizaa.scanner.core", "org.slizaa.scanner.core.spi-api").versionAsInProject(),
        mavenBundle("org.slizaa.scanner.assembly", "org.slizaa.scanner.eclipse").versionAsInProject(),
        bundle("reference:" + jtypeFile.toURI().toString()));
  }

  /**
   * <p>
   * </p>
   * 
   * @throws BundleException
   */
  protected void startAllBundles() throws BundleException {
    for (Bundle bundle : bundleContext.getBundles()) {
      bundle.start();
    }
  }
}