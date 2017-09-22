package org.slizaa.scanner.itest.jtype.simple.example;

import org.neo4j.graphdb.GraphDatabaseService;

public class ExampleInvokesMethod {

  private GraphDatabaseService _service;

  public void exampleInvokesMethod() {
    _service.beginTx();
  }
}
