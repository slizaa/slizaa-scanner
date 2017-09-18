package org.slizaa.scanner.jtype.bytecode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.api.model.resource.CoreModelRelationshipType;
import org.slizaa.scanner.jtype.model.JTypeLabel;

import net.bytebuddy.ByteBuddy;

public class ConstructorTest extends AbstractByteBuddyBytecodeTest {

  @Test
  public void constructorTest() {

    INode node = parse(() -> new ByteBuddy().subclass(Object.class));

    assertThat(node.getRelationships(CoreModelRelationshipType.CONTAINS)).hasSize(1);

    //
    INode fieldNode = node.getRelationships(CoreModelRelationshipType.CONTAINS).get(0).getTargetBean();

    assertThat(fieldNode.getLabels()).containsOnly(JTypeLabel.METHOD, JTypeLabel.CONSTRUCTOR);
    assertThat(fieldNode.getProperty("name")).isEqualTo("<init>");
  }
}
