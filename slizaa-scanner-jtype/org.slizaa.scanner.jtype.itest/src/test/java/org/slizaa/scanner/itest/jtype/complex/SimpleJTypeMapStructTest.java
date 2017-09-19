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

import static org.slizaa.scanner.itest.jtype.fwk.SystemDefinitionUtils.getSystemDefinition_BinariesOnly;

import java.io.IOException;

import org.junit.Test;
import org.neo4j.kernel.api.exceptions.KernelException;
import org.slizaa.scanner.itest.jtype.AbstractJTypeParserTest;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;

/**
  */
public class SimpleJTypeMapStructTest extends AbstractJTypeParserTest {

  /**
   * <p>
   * </p>
   * 
   * @throws KernelException
   * @throws IOException
   */
  @Test
  public void test() throws KernelException, IOException {

    //
    // //
    // System.out.println("Done.\n");
    //
    // System.out.println("Press ENTER to quit.");
    // System.in.read();
  }

  @Override
  protected ISystemDefinition getSystemDefinition() {
    return getSystemDefinition_BinariesOnly();
  }
}
