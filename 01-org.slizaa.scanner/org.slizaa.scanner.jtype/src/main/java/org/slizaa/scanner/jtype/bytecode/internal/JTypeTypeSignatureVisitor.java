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
package org.slizaa.scanner.jtype.bytecode.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureVisitor;
import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.jtype.model.JTypeModelRelationshipType;

/**
 * <p>
 * </p>
 */
public final class JTypeTypeSignatureVisitor extends SignatureVisitor {

  /** - */
  private IModifiableNode         _typeBean;

  /** - */
  private TypeLocalReferenceCache _classLocalTypeReferenceCache;

  /**
   * <p>
   * Creates a new instance of type {@link JTypeTypeSignatureVisitor}.
   * </p>
   */
  public JTypeTypeSignatureVisitor(IModifiableNode typeBean, TypeLocalReferenceCache classLocalTypeReferenceCache) {
    super(Opcodes.ASM6);

    //
    _typeBean = checkNotNull(typeBean);
    _classLocalTypeReferenceCache = checkNotNull(classLocalTypeReferenceCache);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visitClassType(final String name) {
    _classLocalTypeReferenceCache.addTypeReference(_typeBean, name, JTypeModelRelationshipType.USES);
  }

  @Override
  public void visitInnerClassType(String name) {
    System.out.println("visitInnerClassType " + name);
  }

  @Override
  public void visitFormalTypeParameter(String name) {
    //
  }

  @Override
  public SignatureVisitor visitClassBound() {
    return this;
  }

  @Override
  public SignatureVisitor visitInterfaceBound() {
    return this;
  }

  @Override
  public SignatureVisitor visitSuperclass() {
    return this;
  }

  @Override
  public SignatureVisitor visitInterface() {
    return this;
  }

  @Override
  public SignatureVisitor visitParameterType() {
    return this;
  }

  @Override
  public SignatureVisitor visitReturnType() {
    return this;
  }

  @Override
  public SignatureVisitor visitExceptionType() {
    return this;
  }

  @Override
  public SignatureVisitor visitArrayType() {
    return this;
  }

  @Override
  public SignatureVisitor visitTypeArgument(char wildcard) {
    return this;
  }
}
