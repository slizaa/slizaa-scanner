package org.slizaa.scanner.jtype.bytecode.internal;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JTypeAnnotationVisitor extends AnnotationVisitor {

  /**
   * <p>
   * Creates a new instance of type {@link JTypeAnnotationVisitor}.
   * </p>
   */
  public JTypeAnnotationVisitor() {
    super(Opcodes.ASM6);
  }

  @Override
  public void visit(String name, Object value) {
    // System.out.println("JTypeAnnotationVisitor.visit " + name + " : " + value);
    super.visit(name, value);
  }

  @Override
  public void visitEnum(String name, String desc, String value) {
    // System.out.println("JTypeAnnotationVisitor.visitEnum " + name + " : " + value);
    super.visitEnum(name, desc, value);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String name, String desc) {
    // System.out.println("visitAnnotation " + name + " : " + desc);
    return super.visitAnnotation(name, desc);
  }

  @Override
  public AnnotationVisitor visitArray(String name) {
    // System.out.println("visitArray " + name);
    return super.visitArray(name);
  }

}
