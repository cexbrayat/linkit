package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import models.Sponsor;
import models.Staff;

import java.lang.reflect.Type;

/**
 * @author: jripault
 */
public class StaffSerializer extends AbstractMemberSerializer<Staff>{
    @Override
    public JsonElement serialize(Staff member, Type type, JsonSerializationContext jsonSerializationContext) {
        return super.serialize(member, type, jsonSerializationContext);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
