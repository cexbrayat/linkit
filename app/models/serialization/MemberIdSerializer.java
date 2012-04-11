package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import models.Member;

import java.lang.reflect.Type;

/**
 * @author: jripault
 */
public class MemberIdSerializer extends AbstractMemberIdSerializer<Member> {
    @Override
    public JsonElement serialize(Member member, Type type, JsonSerializationContext jsonSerializationContext) {
        return super.serialize(member, type, jsonSerializationContext);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
