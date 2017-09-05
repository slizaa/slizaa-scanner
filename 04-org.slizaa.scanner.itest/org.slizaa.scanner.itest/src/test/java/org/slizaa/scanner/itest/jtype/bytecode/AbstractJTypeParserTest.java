/*******************************************************************************
 * Copyright (c) 2011-2015 Slizaa project team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Slizaa project team - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.itest.jtype.bytecode;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slizaa.scanner.importer.internal.parser.ModelImporter;
import org.slizaa.scanner.itest.framework.TestFrameworkUtils;
import org.slizaa.scanner.jtype.bytecode.JTypeByteCodeParserFactory;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.spi.parser.IParserFactory;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

public abstract class AbstractJTypeParserTest {

  /** - */
  protected static GraphDatabaseService _graphDb;

  /** - */
  private Transaction                 _transaction;

  @Before
  public void before() {

    //
    if (_graphDb == null) {
      _graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(new File(parse())).newGraphDatabase();
    }

    //
    if (_transaction != null) {
      _transaction.failure();
      _transaction.close();
    }

    _transaction = _graphDb.beginTx();
  }

  @After
  public void after() {

    _transaction.failure();
    _transaction.close();
    _transaction = null;
  }

  public Label convert(org.slizaa.scanner.api.model.Label label) {
    return Label.label(label.name());
  }

  public RelationshipType convert(org.slizaa.scanner.api.model.RelationshipType relationshipType) {
    return RelationshipType.withName(relationshipType.name());
  }

  protected Result executeStatement(String statement) {
    return _graphDb.execute(checkNotNull(statement));
  }

  protected Result executeStatement(String statement, Map<String, Object> parameters) {
    return _graphDb.execute(checkNotNull(statement), parameters);
  }

  protected List<Node> getNodes(Result result) {
    assertThat(result.columns()).hasSize(1);
    return result.stream().flatMap(map -> map.values().stream()).filter(o -> o instanceof Node).map(o -> (Node) o)
        .collect(Collectors.toList());
  }

  protected Node getSingleNode(Result result) {
    List<Node> nodes = getNodes(result);
    assertThat(nodes).hasSize(1);
    return nodes.get(0);
  }

  protected List<Node> nodes(String statement) {
    return getNodes(executeStatement(statement));
  }

  protected Node singleNode(String statement) {
    return getSingleNode(executeStatement(statement));
  }

  protected List<Node> nodes(String statement, Map<String, Object> parameters) {
    return getNodes(executeStatement(statement, parameters));
  }

  protected Node singleNode(String statement, Map<String, Object> parameters) {
    return getSingleNode(executeStatement(statement, parameters));
  }

  // protected Node getTypeNode(String fqn) {
  // return _graphDb.findNode(convert(JTypeLabel.TYPE), "fqn", fqn);
  // }
  //
  // /**
  // * <p>
  // * </p>
  // *
  // * @param node
  // */
  // protected void assertTypeReference(Node node, RelationshipType relationshipType, String fqn) {
  // assertThat(node.hasRelationship(relationshipType, Direction.OUTGOING), is(true));
  //
  // //
  // for (Relationship relationship : node.getRelationships(relationshipType, Direction.OUTGOING)) {
  // Node typeReference = relationship.getEndNode();
  // System.out.println(typeReference.getProperty("fqn"));
  // if (typeReference.hasLabel(convert(JTypeLabel.TYPE_REFERENCE))
  // && fqn.equals(typeReference.getProperty("fqn"))) {
  // return;
  // }
  // }
  // fail();
  // }
  //
  // protected void assertDataTypeReference(Node node, RelationshipType relationshipType, String fqn) {
  // assertThat(node.hasRelationship(relationshipType, Direction.OUTGOING), is(true));
  //
  // //
  // for (Relationship relationship : node.getRelationships(relationshipType, Direction.OUTGOING)) {
  // Node typeReference = relationship.getEndNode();
  // System.out.println(typeReference.getProperty("fqn"));
  // for (Label label : typeReference.getLabels()) {
  // System.out.println(label);
  // }
  // if (typeReference.hasLabel(convert(JTypeLabel.PRIMITIVE_DATA_TYPE))
  // && fqn.equals(typeReference.getProperty("fqn"))) {
  // return;
  // }
  // }
  // fail();
  // }
  //
  // protected List<Node> getMethods(Node node, String name) {
  //
  // //
  // List<Node> result = new LinkedList<>();
  //
  // //
  // for (Relationship contains : node.getRelationships(Direction.OUTGOING,
  // convert(CoreModelRelationshipType.CONTAINS))) {
  //
  // //
  // if (contains.getEndNode().hasLabel(convert(JTypeLabel.METHOD))
  // && name.equals(contains.getEndNode().getProperty("name"))) {
  //
  // //
  // result.add(contains.getEndNode());
  // }
  // }
  //
  // //
  // return result;
  // }
  //
  // protected List<Node> getFields(Node node, String name) {
  //
  // //
  // List<Node> result = new LinkedList<>();
  //
  // //
  // for (Relationship contains : node.getRelationships(Direction.OUTGOING,
  // convert(CoreModelRelationshipType.CONTAINS))) {
  //
  // //
  // if (contains.getEndNode().hasLabel(convert(JTypeLabel.FIELD))
  // && name.equals(contains.getEndNode().getProperty("name"))) {
  //
  // //
  // result.add(contains.getEndNode());
  // }
  // }
  //
  // //
  // return result;
  // }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected String parse() {

    File databaseDirectory = TestFrameworkUtils.createTempDirectory(AbstractJTypeParserTest.class.getSimpleName());

    //
    ISystemDefinition systemDefinition = getSystemDefinition();

    //
    IParserFactory parserFactory = new JTypeByteCodeParserFactory();

    //
    ModelImporter executer = new ModelImporter(systemDefinition, databaseDirectory, parserFactory);
    executer.parse(null);

    //
    return databaseDirectory.getAbsolutePath();
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected ISystemDefinition getSystemDefinition() {

    //
    ISystemDefinition systemDefinition = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider("jtype", "1.2.3",
        AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(AbstractJTypeParserTest.class.getProtectionDomain().getCodeSource().getLocation().getFile(),
        ResourceType.BINARY);
    systemDefinition.addContentDefinitionProvider(provider);

    // initialize
    systemDefinition.initialize(null);

    //
    return systemDefinition;
  }
}
