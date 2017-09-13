package org.slizaa.scanner.assembly.itest.eclipse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackages;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

import java.io.File;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Config.EncryptionLevel;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.BundleContext;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class SampleTest {

  @Inject
  private BundleContext   bundleContext;

  @Inject
  private IGraphDbFactory graphDbFactory;

  @Configuration
  public Option[] config() {

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
        mavenBundle("org.slizaa.scanner.core", "org.slizaa.scanner.core.eclipse", "1.0.0-SNAPSHOT"),
        mavenBundle("org.slizaa.scanner.core", "org.slizaa.scanner.core.api", "1.0.0-SNAPSHOT"));
  }

  @Test
  public void testDatabaseAndDriver() {

    //
    IGraphDb graphDb = graphDbFactory.createGraphDb(5001, new File("D:\\_schnurz"));
    assertThat(graphDb).isNotNull();

    //
    Config config = Config.build().withEncryptionLevel(EncryptionLevel.NONE).toConfig();
    Driver driver = GraphDatabase.driver("bolt://localhost:5001", config);
    assertThat(driver).isNotNull();
  }
}