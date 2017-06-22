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
package org.slizaa.scanner.itest.jtype.bytecode;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slizaa.scanner.importer.internal.parser.ModelImporter;
import org.slizaa.scanner.importer.spi.content.AnalyzeMode;
import org.slizaa.scanner.importer.spi.parser.IParserFactory;
import org.slizaa.scanner.itest.framework.TestFrameworkUtils;
import org.slizaa.scanner.jtype.model.bytecode.JTypeByteCodeParserFactory;
import org.slizaa.scanner.model.resource.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

import com.google.common.base.Stopwatch;

public class SimpleDirectoryBasedTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {

    //
    File directory = new File("samples");

    //
    File databaseDirectory = TestFrameworkUtils.createTempDirectory(SimpleDirectoryBasedTest.class.getSimpleName());

    //
    ISystemDefinition systemDefinition = getSystemDefinition_BinariesOnly(
        new SystemDefinitionFactory().createNewSystemDefinition(), directory);

    //
    IParserFactory parserFactory = new JTypeByteCodeParserFactory();

    //
    ModelImporter executer = new ModelImporter(systemDefinition, databaseDirectory, parserFactory);

    Stopwatch stopwatch = Stopwatch.createStarted();
    executer.parse(new DummyProgressMonitor());
    stopwatch.stop();
    System.out.printf("DB-Dir: %s, Time elapsed: %s", databaseDirectory.getAbsolutePath(),
        stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public static ISystemDefinition getSystemDefinition_BinariesOnly(ISystemDefinition descr, File directory) {

    // create property string
    File[] children = directory.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File file, String name) {
        return new File(file, name).isFile() && name.endsWith(".jar") && !name.contains("source_");
      }
    });

    //
    for (File file : children) {

      // name
      String name = file.getName();
      int indexOfDot = name.lastIndexOf('.');
      if (indexOfDot != -1) {
        name = name.substring(0, indexOfDot);
      }

      // add new (custom) content provider
      FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(name, "0.0.0",
          AnalyzeMode.BINARIES_ONLY);
      provider.addRootPath(file.getAbsoluteFile(), ResourceType.BINARY);
      descr.addContentDefinitionProvider(provider);
    }

    // initialize
    descr.initialize(null);

    //
    return descr;
  }
}
