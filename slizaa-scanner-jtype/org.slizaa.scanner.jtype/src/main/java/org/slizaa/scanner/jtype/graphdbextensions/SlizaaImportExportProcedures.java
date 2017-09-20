package org.slizaa.scanner.jtype.graphdbextensions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.logging.Log;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaImportExportProcedures {

  @Context
  public GraphDatabaseService _databaseService;

  // This gives us a log instance that outputs messages to the
  // standard log, `neo4j.log`
  @Context
  public Log                  log;

  @Procedure(name = "slizaa.exportDatabase", mode = Mode.READ)
  public void exportDatabase(@Name("file") String filePath) throws IOException {

    try {
      //
      Path targetPath = Paths.get(filePath);
      targetPath.toFile().getParentFile().mkdirs();

      //
      List<String> nodes = _databaseService.getAllNodes().stream()
          .map(node -> node.getId() + " : " + node.getLabels() + " : " + node.getAllProperties())
          .collect(Collectors.toList());

      //
      List<String> relationShips = _databaseService.getAllRelationships().stream()
          .map(relationShip -> relationShip.getId() + " : " + relationShip.getStartNodeId() + " : "
              + relationShip.getEndNodeId() + " : " + relationShip.getAllProperties())
          .collect(Collectors.toList());

      //
      Files.write(targetPath, Stream.concat(nodes.stream(), relationShips.stream())
          .collect(Collectors.toList()));

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public class Output {
    public Node out;
  }
}