package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Member;
import play.libs.Codec;

import java.lang.reflect.Type;

/**
 * @author: jripault
 */
public class AbstractMemberIdSerializer<T extends Member> implements JsonSerializer<T> {

    @Override
    public JsonElement serialize(T member, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", member.id);
        return obj;
    }
}
