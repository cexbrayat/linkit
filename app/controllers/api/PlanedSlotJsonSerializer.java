package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.planning.PlanedSlot;

import java.lang.reflect.Type;

public class PlanedSlotJsonSerializer extends SessionJsonSerializer implements JsonSerializer<PlanedSlot> {

    public PlanedSlotJsonSerializer(boolean details) {
        super(details);
    }

    @Override
    public JsonElement serialize(PlanedSlot slot, Type type, JsonSerializationContext context) {
        JsonObject result = serializeTalk(slot.talk, type, context);
        if (slot.slot != null) {
            result.addProperty("start", slot.slot.getStartDateTime().toString());
            result.addProperty("end", slot.slot.getEndDateTime().toString());
            result.addProperty("room", slot.slot.getRoom().toString());
        }
        return result;
    }
}
