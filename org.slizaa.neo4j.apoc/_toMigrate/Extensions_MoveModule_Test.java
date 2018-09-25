/*******************************************************************************
 * Copyright (c) 2011-2015 Slizaa project team. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Slizaa project team - initial API and implementation
 ******************************************************************************/
package org.slizaa.neo4j.apoc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slizaa.neo4j.testfwk.ContentDefinitionsUtils.simpleBinaryFile;

import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Record;
import org.slizaa.neo4j.testfwk.SlizaaClientRule;
import org.slizaa.neo4j.testfwk.SlizaaTestServerRule;

public class Extensions_MoveModule_Test {

  /** - */
  @ClassRule
  public static SlizaaTestServerRule _server = new SlizaaTestServerRule(simpleBinaryFile("test", "1.2.3",
      Extensions_MoveModule_Test.class.getProtectionDomain().getCodeSource().getLocation().getFile()));

  /** - */
  @Rule
  public SlizaaClientRule            _client = new SlizaaClientRule();

  @Test
  public void test_moveModule_1() {

    // create
    List<Record> records = this._client.getSession().run("CALL slizaa.arch.createGroup('spunk/dunk')").list();

    // test
    records = this._client.getSession().run(
        "MATCH (m:Module {name: 'asdasd', version: '1.2.3'}) MATCH (g:Group {fqn: 'spunk/dunk'}) CALL slizaa.arch.moveModule(m, g) RETURN m,g")
        .list();
    assertThat(records).hasSize(1);
  }
}