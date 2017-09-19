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
package org.slizaa.scanner.itest.jtype.simple;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.slizaa.scanner.itest.jtype.AbstractJTypeParserTest;
import org.slizaa.scanner.itest.jtype.simple.example.ExampleClass;
import org.slizaa.scanner.jtype.model.IMethodNode;
import org.slizaa.scanner.jtype.model.JTypeLabel;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

public class JTypeParserTest extends AbstractJTypeParserTest {

  @Test
  public void testDirectories() {

    Result result = executeStatement("Match (m:MODULE)-[:CONTAINS]->(d:DIRECTORY) return d");
    result.forEachRemaining(map -> System.out.println(((Node) map.get("d")).getAllProperties()));
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testTypeType() {

    Node node = getSingleNode(executeStatement("Match (t:TYPE {fqn: $name}) return t",
        Collections.singletonMap("name", ExampleClass.class.getName())));
    assertThat(node.hasLabel(convert(JTypeLabel.TYPE))).isTrue();
    assertThat(node.hasLabel(convert(JTypeLabel.CLASS))).isTrue();
    assertThat(node.getProperty(IMethodNode.VISIBILITY)).isEqualTo("public");

    // node = getTypeNode(ExampleInterface.class.getName());
    // assertThat(node.hasLabel(convert(JTypeLabel.TYPE)), is(true));
    // assertThat(node.hasLabel(convert(JTypeLabel.INTERFACE)), is(true));
    //
    // node = getTypeNode(ExampleEnum.class.getName());
    // assertThat(node.hasLabel(convert(JTypeLabel.TYPE)), is(true));
    // assertThat(node.hasLabel(convert(JTypeLabel.ENUM)), is(true));
    //
    // node = getTypeNode(ExampleAnnotation.class.getName());
    // assertThat(node.hasLabel(convert(JTypeLabel.TYPE)), is(true));
    // assertThat(node.hasLabel(convert(JTypeLabel.ANNOTATION)), is(true));
  }

  // /**
  // * <p>
  // * </p>
  // */
  // @Test
  // public void testImplements() {
  //
  // Node node = getTypeNode(ExampleClass.class.getName());
  // assertTypeReference(node, convert(JTypeModelRelationshipType.IMPLEMENTS), SuperInterface.class.getName());
  // }
  //
  // /**
  // * <p>
  // * </p>
  // */
  // @Test
  // public void testExtends() {
  //
  // Node node = getTypeNode(ExampleClass.class.getName());
  // assertTypeReference(node, convert(JTypeModelRelationshipType.EXTENDS), SuperClass.class.getName());
  //
  // node = getTypeNode(ExampleInterface.class.getName());
  // assertTypeReference(node, convert(JTypeModelRelationshipType.EXTENDS), "java.lang.Object");
  // assertTypeReference(node, convert(JTypeModelRelationshipType.EXTENDS), SuperInterface.class.getName());
  // }
  //
  // /**
  // * <p>
  // * </p>
  // */
  // @Test
  // public void testAbstract() {
  //
  // Node node = getTypeNode(AbstractExampleClass.class.getName());
  // assertThat(node.hasProperty("abstract"), is(true));
  //
  // node = getTypeNode(ExampleInterface.class.getName());
  // assertThat(node.hasProperty("abstract"), is(true));
  // }
  //
  @Test
  public void testField() {

    List<Node> nodes = getNodes(executeStatement("Match (t:TYPE {fqn: $name})-[:CONTAINS]->(f:FIELD) return f",
        Collections.singletonMap("name", ExampleClass.class.getName())));

    //
    for (Node node : nodes) {
      System.out.println("FIELD: " + node);
    }

    // // Feld: private Serializable _serializable;
    // List<Node> fields = getFields(node, "_serializable");
    // assertThat(fields.size(), is(1));
    // assertTypeReference(fields.get(0), convert(JTypeModelRelationshipType.IS_OF_TYPE), "java.io.Serializable");
  }
  //
  // @Test
  // public void testArrayField() {
  //
  // Node node = getTypeNode(ExampleClassWithArrays.class.getName());
  //
  // List<Node> fields = getFields(node, "field_1");
  // assertThat(fields.size(), is(1));
  // assertDataTypeReference(fields.get(0), convert(JTypeModelRelationshipType.IS_OF_TYPE), "int");
  //
  // fields = getFields(node, "field_2");
  // assertThat(fields.size(), is(1));
  // assertDataTypeReference(fields.get(0), convert(JTypeModelRelationshipType.IS_OF_TYPE), "boolean");
  // }
  //
  // @Test
  // public void testArrayMethod() {
  //
  // Node node = getTypeNode(ExampleClassWithArrays.class.getName());
  //
  // List<Node> methods = getMethods(node, "test");
  // assertThat(methods.size(), is(1));
  // assertDataTypeReference(methods.get(0), convert(JTypeModelRelationshipType.HAS_PARAMETER), "int");
  // }
  //
  // @Test
  // public void testConstructor() {
  //
  // Node node = getTypeNode(ExampleClass.class.getName());
  // assertThat(getMethods(node, "<init>").size(), is(2));
  //
  // for (Node methodNode : getMethods(node, "<init>")) {
  //
  // System.out.println(
  // " - " + methodNode.getSingleRelationship(convert(JTypeModelRelationshipType.RETURNS), Direction.OUTGOING));
  // }
  // }

  @AfterClass
  public static void afterClass() {
    _graphDb.shutdown();
    _graphDb = null;
  }

  /**
   * @see org.slizaa.scanner.itest.jtype.AbstractJTypeParserTest#shutDownDatabaseAfterTest()
   */
  @Override
  protected boolean shutDownDatabaseAfterTest() {
    return false;
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
