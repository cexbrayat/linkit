package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Staff;

import java.lang.reflect.Type;

public class StaffJsonSerializer extends AbstractMemberJsonSerializer implements JsonSerializer<Staff> {

    @Override
    public JsonElement serialize(Staff staff, Type typeOfSrc, JsonSerializationContext context) {
        return serializeMember(staff, typeOfSrc, context);
    }
}
