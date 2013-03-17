package controllers.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import helpers.JSON;
import models.Member;
import models.SharedLink;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Type;

public abstract class AbstractMemberJsonSerializer {

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

        if (CollectionUtils.isNotEmpty(member.links)) {
            result.add("links", JSON.toJsonArrayOfIds(member.links));
        }
        if (CollectionUtils.isNotEmpty(member.linkers)) {
            result.add("linkers", JSON.toJsonArrayOfIds(member.linkers));
        }
        if (CollectionUtils.isNotEmpty(member.interests)) {
            result.add("linkers", JSON.toJsonArrayOfIds(member.interests));
        }
        if (CollectionUtils.isNotEmpty(member.sharedLinks)) {
            JsonArray links = new JsonArray();
            SharedLinkJsonSerializer linkSerializer = new SharedLinkJsonSerializer();
            for (SharedLink link : member.sharedLinks) {
                links.add(linkSerializer.serialize(link, type, jsonSerializationContext));
            }
            result.add("sharedLinks", links);
        }

        return result;
    }
}
