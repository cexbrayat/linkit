package models.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import models.Member;
import play.libs.Codec;

import java.lang.reflect.Type;

/**
 * @author: jripault
 */
public class AbstractMemberSerializer<T extends Member> implements JsonSerializer<T> {
    private static final String GRAVATAR = "http://www.gravatar.com/avatar/";

    @Override
    public JsonElement serialize(T member, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", member.id);
        obj.addProperty("firstname", member.firstname);
        obj.addProperty("lastname", member.lastname);
        obj.addProperty("login", member.login);
        obj.addProperty("company", member.company);
        obj.addProperty("shortdesc", member.shortDescription);
        obj.addProperty("longdesc", member.longDescription);
        obj.addProperty("urlImage", getUrlImage(member.email));
        obj.addProperty("nbConsult", member.getNbLooks());
        return obj;
    }
    
    private static String getUrlImage(String email)
    {
        StringBuffer url = new StringBuffer();
        url.append(GRAVATAR);
        url.append(Codec.hexMD5(email));
        url.append("?s=20&d=mm&r=pg");
        return url.toString();
    }
}
