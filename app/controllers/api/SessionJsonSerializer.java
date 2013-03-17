package controllers.api;

import com.google.gson.JsonObject;
import helpers.JSON;
import models.Session;
import org.apache.commons.collections.CollectionUtils;

public abstract class SessionJsonSerializer {

    public JsonObject serializeSession(Session session) {
        JsonObject result = new JsonObject();
        result.addProperty("id", session.id);
        result.addProperty("title", session.title);
        result.addProperty("summary", session.summary);
        result.addProperty("description", session.description);

        if (CollectionUtils.isNotEmpty(session.interests)) {
            result.add("interests", JSON.toJsonArrayOfIds(session.interests));
        }

        if (CollectionUtils.isNotEmpty(session.speakers)) {
            result.add("speakers", JSON.toJsonArrayOfIds(session.speakers));
        }

        return result;
    }
}
