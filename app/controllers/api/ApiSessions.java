package controllers.api;

import models.ConferenceEvent;
import models.LightningTalk;
import models.Talk;
import play.mvc.Controller;

import java.util.List;

public class ApiSessions extends Controller {

    public static void talks() {
        List<Talk> talks = Talk.findAllValidatedOn(ConferenceEvent.CURRENT);
        Controller.renderJSON(talks, new TalkJsonSerializer());
    }

    public static void lightningTalks() {
        List<LightningTalk> talks = LightningTalk.findAllOn(ConferenceEvent.CURRENT);
        Controller.renderJSON(talks, new LightningTalkJsonSerializer());
    }
}
