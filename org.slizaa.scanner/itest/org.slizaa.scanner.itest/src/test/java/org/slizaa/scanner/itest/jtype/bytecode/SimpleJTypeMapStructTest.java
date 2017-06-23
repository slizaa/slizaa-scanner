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

import static org.slizaa.scanner.itest.framework.SystemDefinitionUtils.getSystemDefinition_BinariesOnly;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.importer.internal.parser.ModelImporter;
import org.slizaa.scanner.itest.framework.TestFrameworkUtils;
import org.slizaa.scanner.jtype.model.bytecode.JTypeByteCodeParserFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

import com.google.common.base.Stopwatch;

public class SimpleJTypeMapStructTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {

    Logger logger = LoggerFactory.getLogger(SimpleJTypeMapStructTest.class);
    logger.info("Hello World");

    //
    File databaseDirectory = TestFrameworkUtils.createTempDirectory(SimpleJTypeMapStructTest.class.getSimpleName());

    //
    ISystemDefinition systemDefinition = getSystemDefinition_BinariesOnly(new SystemDefinitionFactory()
        .createNewSystemDefinition());

    //
    IParserFactory parserFactory = new JTypeByteCodeParserFactory();

    //
    ModelImporter executer = new ModelImporter(systemDefinition, databaseDirectory, parserFactory);

    Stopwatch stopwatch = Stopwatch.createStarted();
    executer.parse(new DummyProgressMonitor());
    stopwatch.stop();
    System.out.printf("Created database: %s", databaseDirectory);
    System.out.printf("Time elapsed: %s", stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }
}
