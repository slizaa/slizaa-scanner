package org.slizaa.scanner.itest.framework.jdeps.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slizaa.scanner.itest.framework.jdeps.IJDepsWrapper;

public class JDepsWrapper implements IJDepsWrapper {

  @Override
  public Map<String, List<String>> analyze(String jarFile) {

    //
    checkNotNull(jarFile);

    CollectionLog log = new CollectionLog();

    // String javaHome = System.getProperty("java.home");
    // String jdepsBin = javaHome + File.separator + "bin" + File.separator + "jdeps.exe";

    File jdepsFile = new File(System.getProperty("java.home"), "../bin/jdeps.exe");
    List<String> command = new ArrayList<>();
    command.add(jdepsFile.getAbsolutePath());
    command.add("-verbose:class");
    command.add("-filter:none");
    command.add(jarFile);

    ProcessExecutor.run("jdeps", command, log);

    return log.getResult();
  }
}
