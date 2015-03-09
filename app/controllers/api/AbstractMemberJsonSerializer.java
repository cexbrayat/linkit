package controllers.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import helpers.JSON;
import models.Member;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Type;

public abstract class AbstractMemberJsonSerializer {

    private boolean details;

    protected AbstractMemberJsonSerializer(boolean details) {
        this.details = details;
    }

    public JsonElement serializeMember(Member member, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();

        result.addProperty("id", member.id);
        result.addProperty("firstname", member.firstname);
        result.addProperty("lastname", member.lastname);
        result.addProperty("login", member.login);
        result.addProperty("company", member.company);
        result.addProperty("shortdesc", member.shortDescription);
        result.addProperty("longdesc", member.longDescription);
        result.addProperty("urlimage", member.getUrlImage());
        result.addProperty("nbConsults", member.nbConsults);

        if (CollectionUtils.size(member.sharedLinks) != 0) {
            result.add("sharedLinks", jsonSerializationContext.serialize(member.sharedLinks));
        }

        if (CollectionUtils.size(member.interests) != 0) {
            if (details) {
                result.add("interests", jsonSerializationContext.serialize(member.interests));
            } else {
                result.add("interests", JSON.toJsonArrayOfIds(member.interests));
            }
        }

        return result;
    }
}
