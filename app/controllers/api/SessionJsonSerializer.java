package controllers.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import helpers.JSON;
import models.Interest;
import models.Member;
import models.Session;
import models.Talk;
import org.apache.commons.collections.CollectionUtils;

public abstract class SessionJsonSerializer {

    private boolean details = false;

    protected SessionJsonSerializer(boolean details) {
        this.details = details;
    }

    public JsonObject serializeSession(Session session) {
        JsonObject result = new JsonObject();
        result.addProperty("id", session.id);
        result.addProperty("title", session.title);
        result.addProperty("summary", session.summary);
        result.addProperty("description", session.description);

        if (CollectionUtils.isNotEmpty(session.interests)) {
            if (details) {
                JsonArray interests = new JsonArray();
                for (Interest i : session.interests) {
                    JsonObject interest = new JsonObject();
                    interest.addProperty("id", i.id);
                    interest.addProperty("name", i.name);
                    interest.addProperty("url", ApiUrl.getInterestUrl(i.id));
                    interests.add(interest);
                }
                result.add("interests", interests);
            } else {
                result.add("interests", JSON.toJsonArrayOfIds(session.interests));
            }
        }

        if (CollectionUtils.isNotEmpty(session.speakers)) {
            if (details) {
                JsonArray speakers = new JsonArray();
                for (Member s : session.speakers) {
                    JsonObject speaker = new JsonObject();
                    speaker.addProperty("id", s.id);
                    speaker.addProperty("firstname", s.firstname);
                    speaker.addProperty("lastname", s.lastname);
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

    public JsonObject serializeTalk(Talk talk) {
        JsonObject result = serializeSession(talk);

        result.addProperty("format", talk.format.toString());
        result.addProperty("level", talk.level.toString());
        if (talk.track != null) {
            result.addProperty("track", talk.track.toString());
        }

        return result;
    }

}
