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
/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2011 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.slizaa.scanner.jtype.model.internal.bytecode;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;

/**
 * A {@link SignatureVisitor} that prints a disassembled view of the signature it visits.
 * 
 * @author Eugene Kuleshov
 * @author Eric Bruneton
 */
public final class JTypeSignatureVisitor extends SignatureVisitor {

  public JTypeSignatureVisitor(final int access) {
    super(Opcodes.ASM5);
  }

  private JTypeSignatureVisitor(final StringBuffer buf) {
    super(Opcodes.ASM5);
  }

  @Override
  public void visitFormalTypeParameter(final String name) {
    // System.out.println("visitFormalTypeParameter " + name);
  }

  @Override
  public SignatureVisitor visitClassBound() {
    // System.out.println("visitClassBound");
    return this;
  }

  @Override
  public SignatureVisitor visitInterfaceBound() {
    // System.out.println("visitInterfaceBound");
    return this;
  }

  @Override
  public SignatureVisitor visitSuperclass() {
    // System.out.println("visitSuperclass");
    return this;
  }

  @Override
  public SignatureVisitor visitInterface() {
    // System.out.println("visitInterface");
    return this;
  }

  @Override
  public SignatureVisitor visitParameterType() {
    // System.out.println("visitParameterType");
    return this;
  }

  @Override
  public SignatureVisitor visitReturnType() {
    // System.out.println("visitReturnType");
    return this;
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    // System.out.println("visitExceptionType");
    return this;
  }

  @Override
  public void visitBaseType(final char descriptor) {
    // switch (descriptor) {
    // case 'V':
    // System.out.println("void");
    // break;
    // case 'B':
    // System.out.println("byte");
    // break;
    // case 'J':
    // System.out.println("long");
    // break;
    // case 'Z':
    // System.out.println("boolean");
    // break;
    // case 'I':
    // System.out.println("int");
    // break;
    // case 'S':
    // System.out.println("short");
    // break;
    // case 'C':
    // System.out.println("char");
    // break;
    // case 'F':
    // System.out.println("float");
    // break;
    // // case 'D':
    // default:
    // System.out.println("double");
    // break;
    // }
  }

  @Override
  public void visitTypeVariable(final String name) {
    // System.out.println("visitTypeVariable " + name);
  }

  @Override
  public SignatureVisitor visitArrayType() {
    // System.out.println("visitArrayType");
    return this;
  }

  @Override
  public void visitClassType(final String name) {
    // System.out.println("visitClassType " + name);
  }

  @Override
  public void visitInnerClassType(final String name) {
    // System.out.println("visitInnerClassType " + name);
  }

  @Override
  public void visitTypeArgument() {
    // System.out.println("visitTypeArgument");
  }

  @Override
  public SignatureVisitor visitTypeArgument(final char tag) {
    // System.out.println("visitTypeArgument " + tag);
    return this;
  }

  @Override
  public void visitEnd() {
    // System.out.println("visitEnd");
  }
}
