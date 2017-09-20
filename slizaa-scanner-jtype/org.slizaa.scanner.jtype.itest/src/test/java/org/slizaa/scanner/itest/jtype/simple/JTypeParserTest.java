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
import static org.slizaa.scanner.core.testfwk.junit.ContentDefinitionsUtils.simpleBinaryFile;

import java.util.Collections;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;
import org.slizaa.scanner.core.testfwk.junit.SlizaaClientRule;
import org.slizaa.scanner.core.testfwk.junit.SlizaaTestServerRule;
import org.slizaa.scanner.itest.jtype.simple.example.ExampleClass;
import org.slizaa.scanner.itest.jtype.simple.example.ExampleInterface;
import org.slizaa.scanner.jtype.model.ITypeNode;

public class JTypeParserTest {

  @ClassRule
  public static SlizaaTestServerRule _server = new SlizaaTestServerRule(simpleBinaryFile("jtype", "1.2.3",
      JTypeParserTest.class.getProtectionDomain().getCodeSource().getLocation().getFile()));

  @Rule
  public SlizaaClientRule            _client = new SlizaaClientRule();

  /**
   * <p>
   * </p>
   */
  @Test
  public void testTypeType() {

    //
    StatementResult statementResult = _client.getSession().run("Match (t:TYPE {fqn: $name }) return t",
        Collections.singletonMap("name", ExampleInterface.class.getName()));
    Node node = statementResult.single().get(0).asNode();

    // asserts
    assertThat(node.labels()).containsOnly("TYPE", "INTERFACE");
    assertThat(node.asMap()).containsEntry(ITypeNode.FQN, ExampleInterface.class.getName());
    assertThat(node.asMap()).containsEntry(ITypeNode.NAME, ExampleInterface.class.getSimpleName());
    assertThat(node.asMap()).containsEntry(ITypeNode.VISIBILITY, "public");
    assertThat(node.asMap()).containsEntry(ITypeNode.SOURCE_FILE_NAME, "ExampleInterface.java");
    assertThat(node.asMap()).containsEntry(ITypeNode.CLASS_VERSION, "52");
    assertThat(node.asMap()).containsEntry(ITypeNode.ABSTRACT, true);

    // Node node = getSingleNode(executeStatement("Match (t:TYPE {fqn: $name}) return t",
    // Collections.singletonMap("name", ExampleClass.class.getName())));
    // assertThat(node.hasLabel(convert(JTypeLabel.TYPE))).isTrue();
    // assertThat(node.hasLabel(convert(JTypeLabel.CLASS))).isTrue();
    // assertThat(node.getProperty(IMethodNode.VISIBILITY)).isEqualTo("public");

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

    //
    StatementResult statementResult = _client.getSession().run(
        "Match (t:TYPE {fqn: $name})-[:CONTAINS]->(f:FIELD) return f",
        Collections.singletonMap("name", ExampleClass.class.getName()));
    List<Node> nodes = statementResult.list(rec -> rec.get(0).asNode());

    //
    assertThat(nodes).hasSize(1);
    assertThat(nodes.get(0).labels()).containsExactly("FIELD");
    assertThat(nodes.get(0).asMap()).containsEntry("name", "_serializable");
    System.out.println(nodes.get(0));

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
}
