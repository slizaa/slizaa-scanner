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
import java.util.Collections;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import org.slizaa.scanner.core.testfwk.junit.SlizaaClientRule;
import org.slizaa.scanner.core.testfwk.junit.SlizaaTestServerRule;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

/**
 */
public class SimpleJTypeJDKTest {

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

    //
    _client.getSession().run("CALL slizaa.exportDatabase({fileName})",
        Collections.singletonMap("fileName", "C:\\tmp\\exportDatabase.txt")).summary();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static ISystemDefinition getSystemDefinition() {

    //
    ISystemDefinition systemDefinition = new SystemDefinitionFactory().createNewSystemDefinition();

    // create property string
    String version = System.getProperty("java.version");
    String classpath = System.getProperty("sun.boot.class.path");

    //
    for (String path : classpath.split(File.pathSeparator)) {

      if (new File(path).exists()) {
        // name
        String name = path.substring(path.lastIndexOf(File.separator) + 1);
        int indexOfDot = name.lastIndexOf('.');
        if (indexOfDot != -1) {
          name = name.substring(0, indexOfDot);
        }

        // add new (custom) content provider
        FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider("jdk-" + name, version,
            AnalyzeMode.BINARIES_ONLY);
        provider.addRootPath(path, ResourceType.BINARY);
        systemDefinition.addContentDefinitionProvider(provider);
      }
    }

    // initialize
    systemDefinition.initialize(null);

    //
    return systemDefinition;
  }
}
