package models.serialization;

import com.google.gson.*;
import models.*;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author: jripault
 */
public class SessionIdSerializer<T extends Session> implements JsonSerializer<T> {
    @Override
    public JsonElement serialize(T talk, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", talk.id);
        return obj;
    }
}
