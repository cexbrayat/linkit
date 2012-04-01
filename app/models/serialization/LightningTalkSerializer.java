package models.serialization;

import com.google.gson.*;
import controllers.Security;
import models.Interest;
import models.LightningTalk;
import models.Member;
import models.Vote;
import play.Logger;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author: jripault
 */
public class LightningTalkSerializer implements JsonSerializer<LightningTalk> {


    private Member member;

    public LightningTalkSerializer(Member member) {
        this.member = member;
    }

    @Override
    public JsonElement serialize(LightningTalk talk, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", talk.id);
        obj.addProperty("title", talk.title);
        obj.addProperty("summary", talk.summary);
        obj.addProperty("description", talk.description);
        obj.addProperty("nbVotes", talk.getNumberOfVotes());

        if(member != null){
            Vote vote = Vote.findVote(talk, member);
            obj.addProperty("myVote", vote == null ? false : vote.value);
        }

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
