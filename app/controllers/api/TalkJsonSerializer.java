package controllers.api;

import com.google.gson.*;
import helpers.JSON;
import models.Talk;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Type;

public class TalkJsonSerializer implements JsonSerializer<Talk> {

    @Override
    public JsonElement serialize(Talk talk, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", talk.id);
        result.addProperty("title", talk.title);
        result.addProperty("summary", talk.summary);
        result.addProperty("format", talk.format.toString());
        result.addProperty("level", talk.level.toString());
        if (talk.track != null) {
            result.addProperty("track", talk.track.toString());
        }
        result.addProperty("description", talk.description);

        if (CollectionUtils.isNotEmpty(talk.interests)) {
            result.add("interests", JSON.toJsonArrayOfIds(talk.interests));
        }

        if (CollectionUtils.isNotEmpty(talk.speakers)) {
            result.add("speakers", JSON.toJsonArrayOfIds(talk.speakers));
        }

        return result;
    }
}
