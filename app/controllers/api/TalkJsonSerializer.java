package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Talk;

import java.lang.reflect.Type;

public class TalkJsonSerializer extends SessionJsonSerializer implements JsonSerializer<Talk> {

    public TalkJsonSerializer(boolean details) {
        super(details);
    }

    @Override
    public JsonElement serialize(Talk talk, Type type, JsonSerializationContext jsonSerializationContext) {
        return serializeTalk(talk);
    }
}
