package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Member;

import java.lang.reflect.Type;

/**
 * @author: Samil
 */
public class MemberSerializer implements JsonSerializer<Member> {

    @Override
    public JsonElement serialize(Member member, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", member.id);
        obj.addProperty("firstname", member.firstname);
        obj.addProperty("lastname", member.lastname);
        obj.addProperty("login", member.login);
        obj.addProperty("company", member.company);
        obj.addProperty("shortdesc", member.shortDescription);
        obj.addProperty("longdesc", member.longDescription);
        return obj;
    }
}
