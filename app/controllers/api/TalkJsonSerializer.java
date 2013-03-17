package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Talk;

import java.lang.reflect.Type;

public class TalkJsonSerializer extends SessionJsonSerializer implements JsonSerializer<Talk> {

    @Override
    public JsonElement serialize(Talk talk, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = serializeSession(talk);

        result.addProperty("format", talk.format.toString());
        result.addProperty("level", talk.level.toString());
        if (talk.track != null) {
            result.addProperty("track", talk.track.toString());
        }

        return result;
    }
}
