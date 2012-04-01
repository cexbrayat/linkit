package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import models.LightningTalk;

import java.lang.reflect.Type;

public class LightningTalkIdSerializer extends SessionIdSerializer<LightningTalk> {
    @Override
    public JsonElement serialize(LightningTalk talk, Type type, JsonSerializationContext jsonSerializationContext) {
        return super.serialize(talk, type, jsonSerializationContext);
    }
}
