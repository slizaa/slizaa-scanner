/*******************************************************************************
 * Copyright (c) 2011-2015 Slizaa project team. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Slizaa project team - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.neo4j.apoc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slizaa.scanner.neo4j.testfwk.ContentDefinitionsUtils.simpleBinaryFile;

import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Record;
import org.slizaa.scanner.neo4j.testfwk.SlizaaClientRule;
import org.slizaa.scanner.neo4j.testfwk.SlizaaTestServerRule;

public class ExtensionsTest {

  /** - */
  @ClassRule
  public static SlizaaTestServerRule _server = new SlizaaTestServerRule(simpleBinaryFile("test", "1.2.3",
      ExtensionsTest.class.getProtectionDomain().getCodeSource().getLocation().getFile()));

  /** - */
  @Rule
  public SlizaaClientRule            _client = new SlizaaClientRule();

  @Test
  public void test_createGroup_1() {

    // create
    List<Record> records = this._client.getSession().run("CALL slizaa.arch.createGroup('spunk/dunk')").list();
    assertThat(records).hasSize(1);

    // test
    records = this._client.getSession()
        .run("MATCH p=(g1:Group {name:'spunk'})-[:CONTAINS]->(g2:Group {name:'dunk'}) RETURN p").list();
    assertThat(records).hasSize(1);
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void test_Procedure_1() {

    List<Record> records = this._client.getSession().run("CALL slizaa.arch.createModule('spunk/module_1', '1.0.0' )")
        .list();
    assertThat(records).hasSize(1);

    // test
    records = this._client.getSession()
        .run("MATCH p=(g:Group {name:'spunk'})-[:CONTAINS]->(m:Module {name:'module_1'}) RETURN p").list();
    assertThat(records).hasSize(1);
  }
}