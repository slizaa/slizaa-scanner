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
package org.slizaa.scanner.itest.jtype.simple.example;

import java.io.Serializable;

public class ExampleClass extends SuperClass implements SuperInterface {

  private Serializable _serializable;
  
  public ExampleClass() {
    //
  }
  
  public ExampleClass(Serializable serializable) {
    _serializable = serializable;
  }
}