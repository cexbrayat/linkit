package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import models.Talk;

import java.lang.reflect.Type;

public class TalkIdSerializer extends SessionIdSerializer<Talk> {
    @Override
    public JsonElement serialize(Talk talk, Type type, JsonSerializationContext jsonSerializationContext) {
        return super.serialize(talk, type, jsonSerializationContext);
    }
}
