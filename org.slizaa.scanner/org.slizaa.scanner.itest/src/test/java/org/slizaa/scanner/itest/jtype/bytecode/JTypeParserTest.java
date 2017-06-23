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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.slizaa.scanner.itest.jtype.bytecode.example.AbstractExampleClass;
import org.slizaa.scanner.itest.jtype.bytecode.example.ExampleAnnotation;
import org.slizaa.scanner.itest.jtype.bytecode.example.ExampleClass;
import org.slizaa.scanner.itest.jtype.bytecode.example.ExampleClassWithArrays;
import org.slizaa.scanner.itest.jtype.bytecode.example.ExampleEnum;
import org.slizaa.scanner.itest.jtype.bytecode.example.ExampleInterface;
import org.slizaa.scanner.itest.jtype.bytecode.example.SuperClass;
import org.slizaa.scanner.itest.jtype.bytecode.example.SuperInterface;
import org.slizaa.scanner.jtype.model.JTypeModelElementType;
import org.slizaa.scanner.jtype.model.JTypeModelRelationshipType;
import org.slizaa.scanner.jtype.model.TypeType;

public class JTypeParserTest extends AbstractJTypeParserTest {

  
  @Test
  public void testDirectories() {
  
    Result result = executeStatement("Match (m:MODULE)-[:CONTAINS]->(d:DIRECTORY) return d");

    result.forEachRemaining(m -> System.out.println( ((Node)m.get("d")).getAllProperties() ));
  }
  
  /**
   * <p>
   * </p>
   */
  @Test
  public void testTypeType() {

    Node node = getTypeNode(ExampleClass.class.getName());
    assertThat(node.hasLabel(JTypeModelElementType.TYPE), is(true));
    assertThat(node.hasLabel(TypeType.CLASS), is(true));

    node = getTypeNode(ExampleInterface.class.getName());
    assertThat(node.hasLabel(JTypeModelElementType.TYPE), is(true));
    assertThat(node.hasLabel(TypeType.INTERFACE), is(true));

    node = getTypeNode(ExampleEnum.class.getName());
    assertThat(node.hasLabel(JTypeModelElementType.TYPE), is(true));
    assertThat(node.hasLabel(TypeType.ENUM), is(true));

    node = getTypeNode(ExampleAnnotation.class.getName());
    assertThat(node.hasLabel(JTypeModelElementType.TYPE), is(true));
    assertThat(node.hasLabel(TypeType.ANNOTATION), is(true));
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testImplements() {

    Node node = getTypeNode(ExampleClass.class.getName());
    assertTypeReference(node, JTypeModelRelationshipType.IMPLEMENTS, SuperInterface.class.getName());
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testExtends() {

    Node node = getTypeNode(ExampleClass.class.getName());
    assertTypeReference(node, JTypeModelRelationshipType.EXTENDS,
        SuperClass.class.getName());

    node = getTypeNode(ExampleInterface.class.getName());
    assertTypeReference(node, JTypeModelRelationshipType.EXTENDS, "java.lang.Object");
    assertTypeReference(node, JTypeModelRelationshipType.EXTENDS,
        SuperInterface.class.getName());
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void testAbstract() {

    Node node = getTypeNode(AbstractExampleClass.class.getName());
    assertThat(node.hasProperty("abstract"), is(true));

    node = getTypeNode(ExampleInterface.class.getName());
    assertThat(node.hasProperty("abstract"), is(true));
  }

  @Test
  public void testField() {

    Node node = getTypeNode(ExampleClass.class.getName());
    // Feld: private Serializable _serializable;
    List<Node> fields = getFields(node, "_serializable");
    assertThat(fields.size(), is(1));
    assertTypeReference(fields.get(0), JTypeModelRelationshipType.IS_OF_TYPE, "java.io.Serializable");
  }

  @Test
  public void testArrayField() {

    Node node = getTypeNode(ExampleClassWithArrays.class.getName());

    List<Node> fields = getFields(node, "field_1");
    assertThat(fields.size(), is(1));
    assertDataTypeReference(fields.get(0), JTypeModelRelationshipType.IS_OF_TYPE, "int");
    
    fields = getFields(node, "field_2");
    assertThat(fields.size(), is(1));
    assertDataTypeReference(fields.get(0), JTypeModelRelationshipType.IS_OF_TYPE, "boolean");
  }

  @Test
  public void testArrayMethod() {

    Node node = getTypeNode(ExampleClassWithArrays.class.getName());

    List<Node> methods = getMethods(node, "test");
    assertThat(methods.size(), is(1));
    assertDataTypeReference(methods.get(0), JTypeModelRelationshipType.HAS_PARAMETER, "int");
  }

  @Test
  public void testConstructor() {

    Node node = getTypeNode(ExampleClass.class.getName());
    assertThat(getMethods(node, "<init>").size(), is(2));

    for (Node methodNode : getMethods(node, "<init>")) {

      System.out.println(" - "
          + methodNode.getSingleRelationship(JTypeModelRelationshipType.RETURNS, Direction.OUTGOING));
    }
  }
}
