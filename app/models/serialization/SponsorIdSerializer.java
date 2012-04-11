package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import models.Sponsor;

import java.lang.reflect.Type;

/**
 * @author: jripault
 */
public class SponsorIdSerializer extends AbstractMemberIdSerializer<Sponsor>{
    @Override
    public JsonElement serialize(Sponsor member, Type type, JsonSerializationContext jsonSerializationContext) {
        return super.serialize(member, type, jsonSerializationContext);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
