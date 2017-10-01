package org.slizaa.scanner.systemdefinition.internal;

import java.lang.reflect.Type;

import org.slizaa.scanner.spi.content.support.DefaultVariablePath;

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
public class DefaultVariablePathAdapter
    implements JsonSerializer<DefaultVariablePath>, JsonDeserializer<DefaultVariablePath> {

  /**
   * {@inheritDoc}
   */
  @Override
  public DefaultVariablePath deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {

    JsonObject jsonObject = json.getAsJsonObject();
    JsonPrimitive primitive = (JsonPrimitive) jsonObject.get("path");
    String path = primitive.getAsString();

    // return
    return new DefaultVariablePath(path);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JsonElement serialize(DefaultVariablePath variablePath, Type type, JsonSerializationContext context) {

    JsonObject retValue = new JsonObject();
    retValue.addProperty("path", variablePath.getUnresolvedPath());
    return retValue;
  }
}
