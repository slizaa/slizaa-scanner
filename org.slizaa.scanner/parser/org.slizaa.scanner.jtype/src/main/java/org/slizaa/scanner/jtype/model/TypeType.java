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
package org.slizaa.scanner.jtype.model;

import org.objectweb.asm.Opcodes;
import org.slizaa.scanner.model.Label;

/**
 * <p>
 * Enum that represents the type of a java type.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public enum TypeType implements Label {

  CLASS, INTERFACE, ANNOTATION, ENUM;

  /**
   * <p>
   * Returns the {@link TypeType} for the given ASM access code.
   * </p>
   * 
   * @param access
   *          the given ASM access code.
   * @return the {@link TypeType} for the given ASM access code.
   */
  public static TypeType getTypeType(int access) {

    // handle annotation
    if ((access & Opcodes.ACC_ANNOTATION) == Opcodes.ACC_ANNOTATION) {
      return TypeType.ANNOTATION;
    }
    // handle interface
    else if ((access & Opcodes.ACC_INTERFACE) == Opcodes.ACC_INTERFACE) {
      return TypeType.INTERFACE;
    }
    // handle enum
    else if ((access & Opcodes.ACC_ENUM) == Opcodes.ACC_ENUM) {
      return TypeType.ENUM;
    }
    // handle class
    else {
      return TypeType.CLASS;
    }
  }
}
