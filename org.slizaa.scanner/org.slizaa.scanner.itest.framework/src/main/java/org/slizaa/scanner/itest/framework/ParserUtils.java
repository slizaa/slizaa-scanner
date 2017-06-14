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
package org.slizaa.scanner.itest.framework;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.slizaa.scanner.importer.content.IResource;
import org.slizaa.scanner.importer.parser.IParser;
import org.slizaa.scanner.importer.parser.IParser.ParserType;

public class ParserUtils {

  /**
   * @return
   */
  public static IParser createMockParser(ParserType parserType) {
    IParser parser = mock(IParser.class);
    when(parser.getParserType()).thenReturn(parserType);
    when(parser.canParse(any(IResource.class))).thenReturn(true);
    return parser;
  }
}
