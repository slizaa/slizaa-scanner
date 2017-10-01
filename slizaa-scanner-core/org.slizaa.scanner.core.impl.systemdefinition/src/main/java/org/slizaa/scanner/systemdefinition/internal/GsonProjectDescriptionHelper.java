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
package org.slizaa.scanner.systemdefinition.internal;

import java.util.Map;

import org.slizaa.scanner.spi.content.support.DefaultVariablePath;
import org.slizaa.scanner.systemdefinition.ITempDefinitionProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.bytebuddy.implementation.bytecode.constant.DefaultValue;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GsonProjectDescriptionHelper {

  /**
   * <p>
   * </p>
   * 
   * @param project
   * @return
   */
  public static Gson gson(ClassLoader classLoader, Map<String, String> idClassnameMap) {

    //
    GsonBuilder builder = new GsonBuilder();
    builder.excludeFieldsWithoutExposeAnnotation();
    builder.setPrettyPrinting();
    builder.registerTypeAdapter(ITempDefinitionProvider.class, new ContentDefinitionProviderJsonAdapter(classLoader,
        idClassnameMap));
    builder.registerTypeAdapter(DefaultVariablePath.class, new DefaultVariablePathAdapter());
    builder.registerTypeAdapter(SystemDefinition.class, new SystemDefinition.SystemDefinitionJsonDeserializer());

    //
    return builder.create();
  }
}
