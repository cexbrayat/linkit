package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Member;

import java.lang.reflect.Type;

public class MemberJsonSerializer extends AbstractMemberJsonSerializer implements JsonSerializer<Member> {

    public MemberJsonSerializer(boolean details) {
        super(details);
    }

    @Override
    public JsonElement serialize(Member member, Type typeOfSrc, JsonSerializationContext context) {
        return serializeMember(member, typeOfSrc, context);
    }
}
