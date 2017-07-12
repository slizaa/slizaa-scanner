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
package org.slizaa.scanner.jtype.model.internal.bytecode;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

public class JTypeFieldVisitor extends FieldVisitor {

  /** - */
  private JTypeClassVisitor _classVisitor;

  /**
   * @param recorder
   * @param type
   */
  public JTypeFieldVisitor(JTypeClassVisitor classVisitor) {
    super(Opcodes.ASM5);

    // set the class visitor
    this._classVisitor = classVisitor;
  }

  /**
   * {@inheritDoc}
   */
  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    // TODO!

    //
    return null;
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
    // TODO!

    //
    return null;
  }

  @Override
  public void visitAttribute(Attribute attr) {
    // ignored for now...
  }
}
