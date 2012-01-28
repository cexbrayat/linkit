package models.serialization;

import com.google.gson.*;
import models.Interest;
import models.LightningTalk;
import models.Member;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author: jripault
 */
public class LightningTalkSerializer implements JsonSerializer<LightningTalk> {
    @Override
    public JsonElement serialize(LightningTalk talk, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", talk.id);
        obj.addProperty("title", talk.title);
        obj.addProperty("summary", talk.summary);
        obj.addProperty("description", talk.description);
        obj.addProperty("nbVotes", talk.getNumberOfVotes());

        JsonArray array = new JsonArray();
        Set<Interest> interests = talk.interests;
        for(Interest interest : interests)
        {
            array.add(new JsonPrimitive(interest.id));
        }
        obj.add("interests", array);

        array = new JsonArray();
        Set<Member> members = talk.speakers;
        for(Member member : members)
        {
            array.add(new JsonPrimitive(member.id));
        }
        obj.add("speakers", array);

        return obj;
    }
}
