package org.slizaa.scanner.core.impl.plugins;

import org.neo4j.procedure.Procedure;

public class DummyProceduresClass {

  @Procedure
  public int dummyFunction(int i) {
    return i;
  }
}
