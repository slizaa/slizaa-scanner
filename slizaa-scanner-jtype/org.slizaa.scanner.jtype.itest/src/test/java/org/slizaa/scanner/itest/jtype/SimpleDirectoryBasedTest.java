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
package org.slizaa.scanner.itest.jtype;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import org.slizaa.scanner.core.contentdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.core.testfwk.junit.SlizaaClientRule;
import org.slizaa.scanner.core.testfwk.junit.SlizaaTestServerRule;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.IContentDefinitionProvider;

public class SimpleDirectoryBasedTest {

  @ClassRule
  public static SlizaaTestServerRule _server = new SlizaaTestServerRule(getSystemDefinition());

  @Rule
  public SlizaaClientRule            _client = new SlizaaClientRule();

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {

    //
    StatementResult statementResult = _client.getSession().run("Match (t:TYPE) return count(t)");
    System.out.println(statementResult.single().get(0).asInt());
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static IContentDefinitionProvider getSystemDefinition() {

    //
    File[] children = new File("samples").listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File file, String name) {
        return new File(file, name).isFile() && name.endsWith(".jar") && !name.contains("source_");
      }
    });

    //
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider();

    //
    for (File file : children) {

      // name
      String name = file.getName();
      int indexOfDot = name.lastIndexOf('.');
      if (indexOfDot != -1) {
        name = name.substring(0, indexOfDot);
      }

      // add new (custom) content provider
      provider.createFileBasedContentDefinition(name, "0.0.0", new File[] { file }, null, AnalyzeMode.BINARIES_ONLY);
    }

    //
    return provider;
  }
}
