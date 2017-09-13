/*******************************************************************************
 * Copyright (c) 2011-2015 Slizaa project team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Slizaa project team - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.itest.framework;


import java.io.File;

public class TestFrameworkUtils {

  public static File createTempDirectory() {
    return createTempDirectory("");
  }

  public static File createTempDirectory(String postfix) {
    File directory = new File(System.getProperty("user.dir"), "target/unittests/" + System.currentTimeMillis()
        + postfix);
    directory.mkdirs();
    return directory;
  }

  private static File getMapStructDir() {
    return new File(System.getProperty("user.dir").replace('\\', '/')
        + "/../org.slizaa.scanner.assembly.itest.framework/src/main/resources/mapstruct-1.0.0.Beta1/");
  }

  public static File getMapStructClassesJar() {
    return new File(getMapStructDir(), "mapstruct-1.0.0.Beta1.jar");
  }

  public static File getMapStructClassesDir() {
    return new File(getMapStructDir(), "mapstruct-1.0.0.Beta1");
  }

  public static File getMapStructSourcesZip() {
    return new File(getMapStructDir(), "mapstruct-1.0.0.Beta1-sources.jar");
  }

  public static File getMapStructSourcesDir() {
    return new File(getMapStructDir(), "mapstruct-1.0.0.Beta1-sources");
  }

  public static File getMapStructProcessorClassesJar() {
    return new File(getMapStructDir(), "mapstruct-processor-1.0.0.Beta1.jar");
  }

  public static File getMapStructProcessorClassesDir() {
    return new File(getMapStructDir(), "mapstruct-processor-1.0.0.Beta1");
  }

  public static File getMapStructProcessorSourcesZip() {
    return new File(getMapStructDir(), "mapstruct-processor-1.0.0.Beta1-sources.jar");
  }

  public static File getMapStructProcessorSourcesDir() {
    return new File(getMapStructDir(), "mapstruct-processor-1.0.0.Beta1-sources");
  }

  public static String getMapStructVersion() {
    return "1.0.0.Beta1";
  }

  public static String getMapStructName() {
    return "mapstruct";
  }

  public static String getMapStructProcessorName() {
    return "mapstruct-processor";
  }
}
