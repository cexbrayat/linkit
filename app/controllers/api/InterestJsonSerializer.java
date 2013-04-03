package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Interest;

import java.lang.reflect.Type;

public class InterestJsonSerializer implements JsonSerializer<Interest> {

    @Override
    public JsonElement serialize(Interest i, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject interest = new JsonObject();
        interest.addProperty("id", i.id);
        interest.addProperty("name", i.name);
        interest.addProperty("url", ApiUrl.getInterestUrl(i.id));
        return interest;
    }
}
