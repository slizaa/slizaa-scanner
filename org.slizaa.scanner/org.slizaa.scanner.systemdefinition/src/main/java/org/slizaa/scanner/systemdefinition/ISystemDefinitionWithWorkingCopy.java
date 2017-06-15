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


/**
 * Represents the definition of the system that should be analyzed. A {@link ISystemDefinitionWithWorkingCopy} can be
 * created using the {@link ISystemDefinitionFactory}.
 * </p>
 * <p>
 * 
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ISystemDefinitionWithWorkingCopy extends ISystemDefinition {

  void startWorkingCopy();

  void discardWorkingCopy();

  void commitWorkingCopy();

  boolean isWorkingCopy();

  boolean hasWorkingCopyChanged();
}
