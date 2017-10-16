package org.slizaa.scanner.jtype.jdeps.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ops4j.pax.url.mvn.MavenResolver;
import org.ops4j.pax.url.mvn.MavenResolvers;

public class JDepsWrapper {

  public Map<String, List<String>> analyze(String jarFile) {

    //
    checkNotNull(jarFile);

    CollectionLog log = new CollectionLog();

    File jdepsFile = new File(System.getProperty("java.home"), "../bin/jdeps.exe");
    List<String> command = new ArrayList<>();
    command.add(jdepsFile.getAbsolutePath());
    command.add("-verbose:class");
    command.add("-filter:none");
    command.add(jarFile);

    ProcessExecutor.run("jdeps", command, log);

    return log.getResult();
  }

  public static void main(String[] args) throws Exception {

    //
    MavenResolver mavenResolver = MavenResolvers.createMavenResolver(null, null);
    File jarFile = mavenResolver.resolve("org.mapstruct", "mapstruct", null, null, "1.2.0.CR2");

    //
    JDepsWrapper jdepsWrapper = new JDepsWrapper();

    Map<String, List<String>> map = jdepsWrapper.analyze(jarFile.getAbsolutePath());
    System.out.println(map);
  }
}
