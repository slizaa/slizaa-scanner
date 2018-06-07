package org.slizaa.scanner.neo4j.apoc;

import java.util.Map;
import java.util.stream.Stream;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.helpers.collection.Iterables;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.Procedure;

import apoc.result.RelationshipResult;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class CreateDerived {

  /** - */
  public static final String[] EMPTY_ARRAY = new String[0];

  /** - */
  @Context
  public GraphDatabaseService  db;

  /**
   * <p>
   * </p>
   *
   * @param from
   * @param to
   * @param originalRelationship
   * @return
   */
  @Procedure(name = "slizaa.derivedRelationship", mode = Mode.WRITE)
  public Stream<RelationshipResult> derivedRelationship(@Name("from") Node from, @Name("to") Node to,
      @Name("originalRelationship") Relationship originalRelationship) {

//    if (!existsOutgoingDerivedRelationship(from, to, originalRelationship.getType())) {

      Map<String, Object> properties = originalRelationship.getAllProperties();
      properties.put("derived", true);

      return Stream.of(new RelationshipResult(
          setProperties(from.createRelationshipTo(to, originalRelationship.getType()), properties)));
//    }
//    System.out.println(String.format("SKIP: %s - %s - %s",from.getId(), to.getId(), originalRelationship.getType()));
//    return Stream.of();
  }

  /**
   * <p>
   * </p>
   *
   * @param pc
   * @param p
   * @return
   */
  private <T extends PropertyContainer> T setProperties(T pc, Map<String, Object> p) {
    if (p == null)
      return pc;
    for (Map.Entry<String, Object> entry : p.entrySet())
      pc.setProperty(entry.getKey(), toPropertyValue(entry.getValue()));
    return pc;
  }

  /**
   * <p>
   * </p>
   *
   * @param value
   * @return
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private Object toPropertyValue(Object value) {
    if (value instanceof Iterable) {
      Iterable it = (Iterable) value;
      Object first = Iterables.firstOrNull(it);
      if (first == null)
        return EMPTY_ARRAY;
      return Iterables.asArray(first.getClass(), it);
    }
    return value;
  }

  /**
   * <p>
   * </p>
   *
   * @param n1
   * @param n2
   * @param relationshipType
   * @return
   */
  boolean existsOutgoingDerivedRelationship(Node n1, Node n2, RelationshipType relationshipType) {
    for (Relationship rel : n1.getRelationships(Direction.OUTGOING, relationshipType)) {
      if (rel.getEndNode().equals(n2)) {
        return true;
      }
    }
    return false;
  }
}
