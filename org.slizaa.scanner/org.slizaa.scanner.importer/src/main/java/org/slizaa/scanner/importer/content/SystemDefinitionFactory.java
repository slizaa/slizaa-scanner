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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.slizaa.scanner.importer.internal.content.GsonProjectDescriptionHelper;
import org.slizaa.scanner.importer.internal.content.SystemDefinition;
import org.slizaa.scanner.importer.internal.content.SystemDefinitionWithWorkingCopy;

import com.google.common.io.CharStreams;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public final class SystemDefinitionFactory implements ISystemDefinitionFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public ISystemDefinition createNewSystemDefinition() {
    return new SystemDefinition();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISystemDefinitionWithWorkingCopy createSystemDefinitionWithWorkingCopy(ISystemDefinition systemDefinition) {
    checkState(
        systemDefinition instanceof SystemDefinition,
        String.format("SystemDefinition has to be instance of %s, but is instance of %s.",
            SystemDefinition.class.getName(), systemDefinition.getClass().getName()));

    return new SystemDefinitionWithWorkingCopy((SystemDefinition) systemDefinition);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISystemDefinition loadSystemDefinition(Reader reader) {
    return loadSystemDefinition(reader, this.getClass().getClassLoader(), null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISystemDefinition loadSystemDefinition(Reader reader, ClassLoader classLoader) {
    return loadSystemDefinition(reader, classLoader, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISystemDefinition loadSystemDefinition(Reader reader, ClassLoader classLoader,
      Map<String, String> idClassnameMap) {

    checkNotNull(reader);

    try {

      // read the content
      String content = CharStreams.toString(reader);

      // parse system definition
      SystemDefinition systemDefinition = GsonProjectDescriptionHelper.gson(classLoader, idClassnameMap).fromJson(
          new String(content), SystemDefinition.class);

      //
      return systemDefinition;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reloadSystemDefinition(ISystemDefinition systemDefinition, Reader reader, ClassLoader classLoader,
      Map<String, String> idClassnameMap) {

    checkNotNull(systemDefinition);
    checkNotNull(reader);

    // load new system definition
    SystemDefinition newSystemDefinition = (SystemDefinition) loadSystemDefinition(reader, classLoader, idClassnameMap);

    // merge
    if (systemDefinition instanceof SystemDefinition) {
      ((SystemDefinition) systemDefinition).mergeValues(newSystemDefinition);
    }
    //
    else if (systemDefinition instanceof SystemDefinitionWithWorkingCopy) {

      SystemDefinitionWithWorkingCopy definitionWithWorkingCopy = (SystemDefinitionWithWorkingCopy) systemDefinition;
      if (definitionWithWorkingCopy.isWorkingCopy()) {
        definitionWithWorkingCopy.discardWorkingCopy();
      }
      definitionWithWorkingCopy.getOriginal().mergeValues(newSystemDefinition);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(ISystemDefinition systemDefinition, Writer writer) {
    save(systemDefinition, writer, this.getClass().getClassLoader(), null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(ISystemDefinition systemDefinition, Writer writer, ClassLoader classLoader) {
    save(systemDefinition, writer, classLoader, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(ISystemDefinition systemDefinition, Writer writer, ClassLoader classLoader,
      Map<String, String> idClassnameMap) {

    //
    checkNotNull(writer);

    //
    if (systemDefinition instanceof SystemDefinitionWithWorkingCopy) {
      SystemDefinitionWithWorkingCopy workingCopySystemDefinition = (SystemDefinitionWithWorkingCopy) systemDefinition;
      systemDefinition = workingCopySystemDefinition.getOriginal();
    }

    //
    String jsonString = GsonProjectDescriptionHelper.gson(classLoader, idClassnameMap).toJson(systemDefinition);

    //
    try {
      writer.write(jsonString);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
