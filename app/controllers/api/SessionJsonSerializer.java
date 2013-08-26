package controllers.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import helpers.JSON;
import models.Member;
import models.Session;
import models.Talk;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Type;

public abstract class SessionJsonSerializer {

    private boolean details = false;

    protected SessionJsonSerializer(boolean details) {
        this.details = details;
    }

    public JsonObject serializeSession(Session session, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", session.id);
        result.addProperty("title", session.title);
        result.addProperty("summary", session.summary);
        result.addProperty("description", session.description);
        if (session.lang != null) {
            result.addProperty("language", session.lang.toString());
        }

        if (CollectionUtils.size(session.interests) != 0) {
            if (details) {
                result.add("interests", jsonSerializationContext.serialize(session.interests));
            } else {
                result.add("interests", JSON.toJsonArrayOfIds(session.interests));
            }
        }

        if (CollectionUtils.size(session.speakers) != 0) {
            if (details) {
                JsonArray speakers = new JsonArray();
                for (Member s : session.speakers) {
                    JsonObject speaker = new JsonObject();
                    speaker.addProperty("id", s.id);
                    speaker.addProperty("firstname", s.firstname);
                    speaker.addProperty("lastname", s.lastname);
                    result.addProperty("urlimage", s.getUrlImage());
                    speaker.addProperty("url", ApiUrl.getMemberUrl(s.id));
                    speakers.add(speaker);
                }
                result.add("speakers", speakers);
            } else {
                result.add("speakers", JSON.toJsonArrayOfIds(session.speakers));
            }
        }

        return result;
    }

    public JsonObject serializeTalk(Talk talk, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = serializeSession(talk, type, jsonSerializationContext);

        result.addProperty("format", talk.format.toString());
        result.addProperty("level", talk.level.toString());
        if (talk.track != null) {
            result.addProperty("track", talk.track.toString());
        }

        return result;
    }

}
