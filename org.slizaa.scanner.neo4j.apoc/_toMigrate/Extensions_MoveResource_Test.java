/*******************************************************************************
 * Copyright (c) 2011-2015 Slizaa project team. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Slizaa project team - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.neo4j.apoc;

import static org.slizaa.scanner.neo4j.testfwk.ContentDefinitionsUtils.simpleBinaryFile;

import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.Record;
import org.slizaa.scanner.neo4j.testfwk.SlizaaClientRule;
import org.slizaa.scanner.neo4j.testfwk.SlizaaTestServerRule;

public class Extensions_MoveResource_Test {

  /** - */
  @ClassRule
  public static SlizaaTestServerRule _server = new SlizaaTestServerRule(simpleBinaryFile("test", "1.2.3",
      Extensions_MoveResource_Test.class.getProtectionDomain().getCodeSource().getLocation().getFile()));

  /** - */
  @Rule
  public SlizaaClientRule            _client = new SlizaaClientRule();

  @Test
  public void test_moveResource_1() {

    // create
    List<Record> records = this._client.getSession().run("CALL slizaa.arch.createModule('hurz', '1.0.0')").list();

    // test
    // records = this._client.getSession().run(
    // "MATCH (r:Resource {fqn: 'org/mapstruct/ap/shaded/freemarker/core/RangeModel.class'}) RETURN r")
    // .list();
    records = this._client.getSession().run(
        "MATCH (r:Resource {fqn: 'org/mapstruct/ap/shaded/freemarker/core/RangeModel.class'}) MATCH (m:Module {name: 'hurz', version: '1.0.0'}) CALL slizaa.arch.moveResource(r, m) RETURN r,m")
        .list();

    //
    records.forEach(r -> System.out.println(r.get("r").asMap()));

    // assertThat(records).hasSize(1);
  }
}