package org.slizaa.scanner.jtype.jdeps.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavapWrapper {

  public static void doIt(String classpath, String className) {

    File javapFile = new File(System.getProperty("java.home"), "../bin/javap.exe");

    List<String> command = new ArrayList<>();
    command.add(javapFile.getAbsolutePath());
    command.add("-v");
    command.add("-constants");
    command.add("-cp");
    command.add(classpath);
    command.add(className);

    //
    ProcessExecutor.run("jdeps", command, new Log() {

      @Override
      public void info(CharSequence message) {
        System.out.println(message);
      }

      @Override
      public void error(CharSequence message) {
        System.out.println(message);
      }

      @Override
      public void debug(CharSequence message) {
        System.out.println(message);
      }
    });
  }
  
  public static void main(String[] args) {
    doIt("D:\\50-Development\\environments\\slizaa-master\\ws\\TestReferenceProject\\bin", "test.InnerOuterTest$InnerClass");
  }
}