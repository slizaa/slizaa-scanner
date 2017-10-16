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
package org.slizaa.scanner.jtype.itest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slizaa.scanner.neo4j.testfwk.junit.ContentDefinitionsUtils.multipleBinaryMvnArtifacts;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.kernel.api.exceptions.KernelException;
import org.slizaa.scanner.neo4j.testfwk.junit.SlizaaClientRule;
import org.slizaa.scanner.neo4j.testfwk.junit.SlizaaTestServerRule;

/**
  */
public class SimpleJTypeMapStructTest {

  @ClassRule
  public static SlizaaTestServerRule _server = new SlizaaTestServerRule(
      multipleBinaryMvnArtifacts(new String[] { "org.mapstruct", "mapstruct", "1.0.0.Beta1" },
          new String[] { "org.mapstruct", "mapstruct-processor", "1.0.0.Beta1" }));

  @Rule
  public SlizaaClientRule            _client = new SlizaaClientRule();

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
    StatementResult statementResult = _client.getSession().run("Match (t:TYPE) return count(t)");
    assertThat(statementResult.single().get(0).asInt()).isEqualTo(148);
  }
}
