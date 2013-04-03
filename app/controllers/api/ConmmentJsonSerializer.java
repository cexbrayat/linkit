package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.SessionComment;

import java.lang.reflect.Type;

public class ConmmentJsonSerializer implements JsonSerializer<SessionComment> {

    @Override
    public JsonElement serialize(SessionComment comment, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("date", comment.postedAt.toString());
        result.addProperty("content", comment.content);
        return result;
    }
}
