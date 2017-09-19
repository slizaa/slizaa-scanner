package org.slizaa.scanner.core.impl.plugins;

import org.neo4j.procedure.UserFunction;

public class DummyFunctionsClass {

  @UserFunction
  public int dummyFunction(int i) {
    return i;
  }
}
