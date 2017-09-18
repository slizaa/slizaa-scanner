package org.slizaa.scanner.assembly.itest.eclipse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackages;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.assembly.itest.eclipse.aether.ResolveArtifact;
import org.slizaa.scanner.spi.parser.IParserFactory;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class SampleTest {

  {
    System.setProperty("org.ops4j.pax.url.mvn.localRepository",
        System.getProperty("user.home") + File.separator + ".m2" + File.separator + "repository");
  }

  @Inject
  private BundleContext   bundleContext;

  @Inject
  private IGraphDbFactory graphDbFactory;

  @Configuration
  public Option[] config() {

    //
    File jtypeFile = ResolveArtifact.resolve("org.slizaa.scanner.jtype", "org.slizaa.scanner.jtype", "1.0.0-SNAPSHOT");

    //
    return options(mavenBundle("org.assertj", "assertj-core", "3.8.0"), junitBundles(),

        //
        bootDelegationPackages("sun.*", "com.sun.*"),

        //
        mavenBundle("com.google.guava", "guava", "15.0"),
        mavenBundle("org.eclipse.equinox", "common", "3.6.200-v20130402-1505"),
        mavenBundle("com.google.code.gson", "gson", "2.5"),

        //
        wrappedBundle(mavenBundle("org.neo4j.driver", "neo4j-java-driver", "1.4.3")),

        //
        mavenBundle("org.slizaa.scanner.core", "org.slizaa.scanner.core.api", "1.0.0-SNAPSHOT"),
        mavenBundle("org.slizaa.scanner.assembly", "org.slizaa.scanner.core.eclipse", "1.0.0-SNAPSHOT"),

        //
        bundle("reference:" + jtypeFile.toURI().toString()),

        //
        mavenBundle("io.github.lukehutch", "fast-classpath-scanner", "2.4.7")

    );
  }

  @Test
  public void testDatabaseAndDriver() {

    //
    Bundle jtypeBundle = getBundle("org.slizaa.scanner.jtype");
    assertThat(jtypeBundle).isNotNull();
    ClassLoader classLoader = jtypeBundle.adapt(BundleWiring.class).getClassLoader();



    // TODO: TEMP DIR
    IGraphDb graphDb = graphDbFactory.createGraphDb(5001, new File("C:\\_schnurz"), null);
    assertThat(graphDb).isNotNull();
    
    // //
    // Config config = Config.build().withEncryptionLevel(EncryptionLevel.NONE).toConfig();
    // Driver driver = GraphDatabase.driver("bolt://localhost:5001", config);
    // assertThat(driver).isNotNull();
  }

  /**
   * @param symbolicName
   * @return
   */
  protected Bundle getBundle(String symbolicName) {
    for (Bundle bundle : bundleContext.getBundles()) {
      if (bundle.getSymbolicName().equals(symbolicName)) {
        return bundle;
      }
    }
    return null;
  }
}