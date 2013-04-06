package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Sponsor;

import java.lang.reflect.Type;

public class SponsorJsonSerializer extends AbstractMemberJsonSerializer implements JsonSerializer<Sponsor> {

    public SponsorJsonSerializer(boolean details) {
        super(details);
    }

    @Override
    public JsonElement serialize(Sponsor sponsor, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = (JsonObject) serializeMember(sponsor, typeOfSrc, context);

        result.addProperty("logo", ApiUrl.getFullUrl(sponsor.logoUrl));
        result.addProperty("level", sponsor.level.toString());

        return result;
    }
}
