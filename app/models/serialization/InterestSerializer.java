package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Interest;

import java.lang.reflect.Type;

/**
 * @author jripault
 */
public class InterestSerializer implements JsonSerializer<Interest> {
    @Override
    public JsonElement serialize(Interest interest, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", interest.id);
        obj.addProperty("name", interest.name);
        return obj;
    }
}
