package org.slizaa.scanner.jtype.bytecode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.jtype.model.JTypeLabel;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;

public class TypeTest extends AbstractBytecodeTest {

  @Test
  public void publicClass() {

    INode node = parse(() -> new ByteBuddy().subclass(Object.class).modifiers(Visibility.PUBLIC));

    assertThat(node.getProperties()).hasSize(4);
    assertThat(node.getProperty("name")).isEqualTo("Type");
    assertThat(node.getProperty("fqn")).isEqualTo("example.Type");
    assertThat(node.getProperty("classVersion")).isEqualTo("52");
    assertThat(node.getProperty("visibility")).isEqualTo("PUBLIC");

    assertThat(node.getLabels()).containsExactly(JTypeLabel.TYPE, JTypeLabel.CLASS);
  }

  @Test
  public void publicInterface() {

    INode node = parse(() -> new ByteBuddy().makeInterface());

    assertThat(node.getProperties()).hasSize(5);
    assertThat(node.getProperty("abstract")).isEqualTo(true);
    assertThat(node.getProperty("name")).isEqualTo("Type");
    assertThat(node.getProperty("fqn")).isEqualTo("example.Type");
    assertThat(node.getProperty("classVersion")).isEqualTo("52");
    assertThat(node.getProperty("visibility")).isEqualTo("PUBLIC");

    assertThat(node.getLabels()).containsExactly(JTypeLabel.TYPE, JTypeLabel.INTERFACE);
  }

  @Test
  public void publicEnum() {

    INode node = parse(() -> new ByteBuddy().makeEnumeration("hurz", "purz"));

    assertThat(node.getProperties()).hasSize(6);
    assertThat(node.getProperty("final")).isEqualTo(true);
    assertThat(node.getProperty("name")).isEqualTo("Type");
    assertThat(node.getProperty("fqn")).isEqualTo("example.Type");
    assertThat(node.getProperty("classVersion")).isEqualTo("52");
    assertThat(node.getProperty("visibility")).isEqualTo("PUBLIC");
    assertThat(node.getProperty("signature")).isEqualTo("Ljava/lang/Enum<Lexample/Type;>;");

    assertThat(node.getLabels()).containsExactly(JTypeLabel.TYPE, JTypeLabel.ENUM);
  }
  
  @Test
  public void publicAnnotation() {

    INode node = parse(() -> new ByteBuddy().makeAnnotation());

    assertThat(node.getProperties()).hasSize(5);
    assertThat(node.getProperty("abstract")).isEqualTo(true);
    assertThat(node.getProperty("name")).isEqualTo("Type");
    assertThat(node.getProperty("fqn")).isEqualTo("example.Type");
    assertThat(node.getProperty("classVersion")).isEqualTo("52");
    assertThat(node.getProperty("visibility")).isEqualTo("PUBLIC");

    assertThat(node.getLabels()).containsExactly(JTypeLabel.TYPE, JTypeLabel.ANNOTATION);
  }
  
  @Test
  public void packagePrivateClass() {

    INode node = parse(() -> new ByteBuddy().subclass(Object.class).modifiers(Visibility.PACKAGE_PRIVATE));

    assertThat(node.getProperties()).hasSize(4);
    assertThat(node.getProperty("name")).isEqualTo("Type");
    assertThat(node.getProperty("fqn")).isEqualTo("example.Type");
    assertThat(node.getProperty("classVersion")).isEqualTo("52");
    assertThat(node.getProperty("visibility")).isEqualTo("PACKAGE_PRIVATE");

    assertThat(node.getLabels()).containsExactly(JTypeLabel.TYPE, JTypeLabel.CLASS);
  }
}
