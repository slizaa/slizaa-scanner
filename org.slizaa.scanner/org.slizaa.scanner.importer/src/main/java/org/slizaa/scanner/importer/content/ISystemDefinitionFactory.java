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
package org.slizaa.scanner.importer.content;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * <p>
 * Factory to create new or load/save existing instances of a {@link ISystemDefinition}.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ISystemDefinitionFactory {

  /**
   * <p>
   * Creates a new (empty) instance of type {@link ISystemDefinition}.
   * </p>
   * 
   * @return a new {@link ISystemDefinition}.
   */
  ISystemDefinition createNewSystemDefinition();

  /**
   * <p>
   * Creates a new instance of type {@link ISystemDefinitionWithWorkingCopy} based on the specified
   * {@link ISystemDefinition}.
   * </p>
   * 
   * @param systemDefinition
   *          the system definition
   * @return a new instance of type {@link ISystemDefinitionWithWorkingCopy}
   */
  ISystemDefinitionWithWorkingCopy createSystemDefinitionWithWorkingCopy(ISystemDefinition systemDefinition);

  /**
   * <p>
   * Loads an {@link ISystemDefinition} from the specified file.
   * </p>
   * 
   * @param reader
   *          the reader
   * 
   * @return the {@link ISystemDefinition} instance.
   */
  ISystemDefinition loadSystemDefinition(Reader reader);

  /**
   * <p>
   * Loads an {@link ISystemDefinition} from the specified file.
   * </p>
   * 
   * @param reader
   * 
   * @param classLoader
   *          the class loader
   * @return the {@link ISystemDefinition} instance.
   */
  ISystemDefinition loadSystemDefinition(Reader reader, ClassLoader classLoader);

  /**
   * <p>
   * Loads an {@link ISystemDefinition} from the specified file.
   * </p>
   * 
   * @param reader
   * @param classLoader
   *          the class loader
   * @param idClassnameMap
   *          the id-classname map
   * 
   * @return the {@link ISystemDefinition} instance.
   */
  ISystemDefinition loadSystemDefinition(Reader reader, ClassLoader classLoader, Map<String, String> idClassnameMap);

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param reader
   * @param classLoader
   * @param idClassnameMap
   */
  void reloadSystemDefinition(ISystemDefinition systemDefinition, Reader reader, ClassLoader classLoader,
      Map<String, String> idClassnameMap);

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param writer
   */
  void save(ISystemDefinition systemDefinition, Writer writer);

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param writer
   * @param classLoader
   */
  void save(ISystemDefinition systemDefinition, Writer writer, ClassLoader classLoader);

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinition
   * @param writer
   * @param classLoader
   * @param idClassnameMap
   */
  void save(ISystemDefinition systemDefinition, Writer writer, ClassLoader classLoader,
      Map<String, String> idClassnameMap);
}
