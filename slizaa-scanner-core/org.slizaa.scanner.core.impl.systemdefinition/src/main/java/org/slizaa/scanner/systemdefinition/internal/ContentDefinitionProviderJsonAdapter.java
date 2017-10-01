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

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slizaa.scanner.systemdefinition.ITempDefinitionProvider;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ContentDefinitionProviderJsonAdapter implements JsonSerializer<ITempDefinitionProvider>,
    JsonDeserializer<ITempDefinitionProvider> {

  /** the CLASSNAME attribute */
  private static final String TYPE     = "provider-id";

  /** the INSTANCE attribute */
  private static final String INSTANCE = "provider-config";

  /** - */
  private ClassLoader         _classLoader;

  /** - */
  private Map<String, String> _classIdMap;

  /** - */
  private Map<String, String> _idClassMap;

  /**
   * <p>
   * Creates a new instance of type {@link ContentDefinitionProviderJsonAdapter}.
   * </p>
   * 
   * @param classLoader
   */
  public ContentDefinitionProviderJsonAdapter(ClassLoader classLoader, Map<String, String> idClassnameMap) {
    checkNotNull(classLoader);

    //
    _classLoader = classLoader;
    _idClassMap = idClassnameMap;

    //
    if (_classIdMap != null) {

      //
      _classIdMap = new HashMap<String, String>();

      //
      for (Entry<String, String> entry : _idClassMap.entrySet()) {
        _classIdMap.put(entry.getValue(), entry.getKey());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonElement serialize(ITempDefinitionProvider src, Type typeOfSrc, JsonSerializationContext context) {

    String className = src.getClass().getName();
    String id = _classIdMap != null && _classIdMap.containsKey(className) ? _classIdMap.get(className) : className;

    JsonObject retValue = new JsonObject();
    retValue.addProperty(TYPE, id);
    JsonElement elem = context.serialize(src);
    retValue.add(INSTANCE, elem);
    return retValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ITempDefinitionProvider deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    JsonObject jsonObject = json.getAsJsonObject();
    JsonPrimitive prim = (JsonPrimitive) jsonObject.get(TYPE);
    String id = prim.getAsString();
    String className = _idClassMap != null && _idClassMap.containsKey(id) ? _idClassMap.get(id) : id;

    Class<?> clazz = null;
    try {
      clazz = _classLoader.loadClass(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new JsonParseException(e.getMessage());
    }

    // return the instance
    return context.deserialize(jsonObject.get(INSTANCE), clazz);
  }
}
