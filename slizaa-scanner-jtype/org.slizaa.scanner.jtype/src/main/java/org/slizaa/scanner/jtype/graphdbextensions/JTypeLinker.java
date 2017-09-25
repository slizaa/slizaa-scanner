package org.slizaa.scanner.jtype.graphdbextensions;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;

public class JTypeLinker {

  @Context
  public GraphDatabaseService db;

  @Context
  public Log                  log;


}