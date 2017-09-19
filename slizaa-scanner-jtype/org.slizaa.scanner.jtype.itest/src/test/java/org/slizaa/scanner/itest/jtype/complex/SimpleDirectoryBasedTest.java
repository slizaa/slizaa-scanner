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
package org.slizaa.scanner.itest.jtype.complex;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.Test;
import org.slizaa.scanner.itest.jtype.AbstractJTypeParserTest;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

public class SimpleDirectoryBasedTest extends AbstractJTypeParserTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {

    // TODO...
  }

  @Override
  protected ISystemDefinition getSystemDefinition() {

    //
    ISystemDefinition systemDefinition = new SystemDefinitionFactory().createNewSystemDefinition();

    // create property string
    File[] children = new File("samples").listFiles(new FilenameFilter() {
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
      systemDefinition.addContentDefinitionProvider(provider);
    }

    // initialize
    systemDefinition.initialize(null);

    //
    return systemDefinition;
  }
}
