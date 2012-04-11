package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Member;
import models.Sponsor;

import java.lang.reflect.Type;

/**
 * @author: jripault
 */
public class SponsorSerializer extends AbstractMemberSerializer<Sponsor>{
    @Override
    public JsonElement serialize(Sponsor member, Type type, JsonSerializationContext jsonSerializationContext) {
        return super.serialize(member, type, jsonSerializationContext);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
