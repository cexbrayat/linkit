package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.SharedLink;

import java.lang.reflect.Type;

public class SharedLinkJsonSerializer implements JsonSerializer<SharedLink> {

    @Override
    public JsonElement serialize(SharedLink link, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();

        result.addProperty("id", link.id);
        result.addProperty("name", link.name);
        result.addProperty("url", link.URL);
        result.addProperty("ordernum", link.ordernum);

        return result;
    }
}
