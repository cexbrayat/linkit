package models.serialization;

import com.google.gson.*;
import models.Member;
import play.Logger;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author: jripault
 */
public class MemberSerializer extends AbstractMemberSerializer<Member> {
    @Override
    public JsonElement serialize(Member member, Type type, JsonSerializationContext jsonSerializationContext) {
        return super.serialize(member, type, jsonSerializationContext);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
