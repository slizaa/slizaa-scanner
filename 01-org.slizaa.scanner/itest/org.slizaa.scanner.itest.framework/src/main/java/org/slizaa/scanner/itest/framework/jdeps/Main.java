package org.slizaa.scanner.itest.framework.jdeps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws Exception {

    Log log = new Log() {

      @Override
      public void info(CharSequence message) {
        System.out.println("--- > " + message);
      }

      @Override
      public void error(CharSequence message) {
        System.out.println("--- > " + message);
      }

      @Override
      public void debug(CharSequence message) {
        System.out.println("--- > " + message);
      }
    };

//    String javaHome = System.getProperty("java.home");
//    String jdepsBin = javaHome + File.separator + "bin" + File.separator + "jdeps.exe";

    List<String> command = new ArrayList<>();
    command.add("C:\\Program Files\\Java\\jdk1.8.0_77\\bin\\jdeps.exe");
    command.add("-verbose:class");
    command.add(
        "C:\\Users\\wuetherich\\.m2\\repository\\org\\slizaa\\org.slizaa.neo4j.dbadapter\\0.0.4-SNAPSHOT\\org.slizaa.neo4j.dbadapter-0.0.4-SNAPSHOT.jar");

    ProcessExecutor.run("jdeps", command, log);
  }
}
