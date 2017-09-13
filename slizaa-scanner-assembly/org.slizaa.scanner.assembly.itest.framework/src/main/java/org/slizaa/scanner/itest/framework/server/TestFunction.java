package org.slizaa.scanner.itest.framework.server;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.UserFunction;

public class TestFunction {

  @Context
  public GraphDatabaseService db;

  // This gives us a log instance that outputs messages to the
  // standard log, `neo4j.log`
  @Context
  public Log                  log;

  @UserFunction
  @Description("apoc.date.currentTimestamp() - returns System.currentTimeMillis()")
  public long currentTimestamp() {
    return System.currentTimeMillis();
  }

  @UserFunction
  @Description("hurz() - returns hurz()")
  public String hurz() {
    return "hurz";
  }

  @Procedure(name = "slizaa.createNewModule", mode = Mode.WRITE)
  public Stream<Output> index(@Name("nodeId") String modulePath) {

    //
    List<Output> result = new LinkedList<>();

    //
    Node node = db.createNode();
    node.setProperty("path", modulePath);
    node.setProperty("hurz", "purz");
    node.setProperty("schnotz", "dotz");

    Output output = new Output();
    output.out = node;
    result.add(output);

    //
    return result.stream();
  }

  @Procedure(name = "slizaa.dump", mode = Mode.READ)
  public void dump(@Name("file") String file) {

    for (Node node : db.getAllNodes()) {
      System.out.println(node.getId() + " : " + node.getLabels());
    }
  }

  public class Output {
    public Node out;
  }

}