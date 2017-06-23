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
package org.slizaa.scanner.systemdefinition;

import java.io.File;

import org.slizaa.scanner.model.resource.ResourceType;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinition;
import org.slizaa.scanner.systemdefinition.internal.SystemDefinitionWithWorkingCopy;

public class TestHelper {

  public static SystemDefinitionWithWorkingCopy createDefaultSystemDefinitionWithWorkingCopy(SystemDefinition original) {
    return new SystemDefinitionWithWorkingCopy(original);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public static SystemDefinitionWithWorkingCopy createDefaultSystemDefinitionWithWorkingCopy() {
    return new SystemDefinitionWithWorkingCopy(createDefaultSystemDefinition());
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public static SystemDefinition createDefaultSystemDefinition() {

    SystemDefinition systemDefinition = (SystemDefinition) new SystemDefinitionFactory().createNewSystemDefinition();

    FileBasedContentDefinitionProvider basedContentDefinitionProvider = new FileBasedContentDefinitionProvider(
        "TestName", "1.2.3.4", AnalyzeMode.BINARIES_ONLY);
    basedContentDefinitionProvider.addRootPath(System.getProperty("user.dir") + File.separatorChar + "target",
        ResourceType.BINARY);

    systemDefinition.addContentDefinitionProvider(basedContentDefinitionProvider);

    basedContentDefinitionProvider = new FileBasedContentDefinitionProvider("TestName", "5.6.7.8",
        AnalyzeMode.BINARIES_ONLY);
    basedContentDefinitionProvider.addRootPath(System.getProperty("user.dir") + File.separatorChar + "target",
        ResourceType.BINARY);

    systemDefinition.addContentDefinitionProvider(basedContentDefinitionProvider);

    //
    return systemDefinition;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public static FileBasedContentDefinitionProvider createContentDefinitionProvider() {

    //
    FileBasedContentDefinitionProvider result = new FileBasedContentDefinitionProvider("TestName", "1.2.3.4",
        AnalyzeMode.BINARIES_ONLY);

    // add root path
    result.addRootPath(System.getProperty("user.dir") + File.separatorChar + "target", ResourceType.BINARY);

    //
    return result;
  }
}
