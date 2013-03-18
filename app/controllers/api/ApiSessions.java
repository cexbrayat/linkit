package controllers.api;

import com.google.gson.JsonSerializer;
import models.ConferenceEvent;
import models.LightningTalk;
import models.Talk;
import play.mvc.Controller;

import java.util.List;

public class ApiSessions extends JsonpController {

    private static JsonSerializer TALK_SERIALIZERS[] = new JsonSerializer[] {
        new TalkJsonSerializer(), new LightningTalkJsonSerializer()
    };

    public static void talks() {
        List<Talk> talks = Talk.findAllValidatedOn(ConferenceEvent.CURRENT);
        renderJSON(talks, TALK_SERIALIZERS);
    }

    public static void talk(long id) {
        Talk talk = Talk.findById(id);
        notFoundIfNull(talk);
        renderJSON(talk, TALK_SERIALIZERS);
    }

    public static void lightningTalks() {
        List<LightningTalk> talks = LightningTalk.findAllOn(ConferenceEvent.CURRENT);
        renderJSON(talks, TALK_SERIALIZERS);
    }

    public static void lightningTalk(long id) {
        LightningTalk talk = LightningTalk.findById(id);
        notFoundIfNull(talk);
        renderJSON(talk, TALK_SERIALIZERS);
    }
}
