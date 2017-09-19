package org.slizaa.scanner.itest.jtype.fwk.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slizaa.scanner.itest.jtype.fwk.IJDepsWrapper;

public class JDepsWrapper implements IJDepsWrapper {

  @Override
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
}
