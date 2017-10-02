package org.slizaa.scanner.jtype.bytecode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.RunWith;
import org.slizaa.scanner.core.spi.parser.model.INode;
import org.slizaa.scanner.jtype.model.JTypeLabel;
import org.slizaa.scanner.jtype.model.JTypeModelRelationshipType;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class AnnotatedTypeTest extends AbstractByteBuddyBytecodeTest {

  @Test
  public void annotatedTypeTest() {

    //
    AnnotationDescription annotationDescription = AnnotationDescription.Builder.ofType(RunWith.class)
        .define("value", ErrorReportingRunner.class).build();

    //
    INode node = parse(() -> new ByteBuddy().subclass(Object.class).annotateType(annotationDescription));

    //
    assertThat(node.getRelationships(JTypeModelRelationshipType.ANNOTATED_BY)).hasSize(1);

    //
    INode annotationNode = node.getRelationships(JTypeModelRelationshipType.ANNOTATED_BY).get(0).getTargetBean();

    System.out.println(annotationNode);

    assertThat(annotationNode.getLabels()).containsOnly(JTypeLabel.ANNOTATION_INSTANCE);
    // assertThat(fieldNode.getProperty("name")).isEqualTo("hurz");
  }
}
