package controllers.api;

import models.ConferenceEvent;
import models.Talk;
import play.mvc.Controller;

import java.util.List;

public class APISessions extends Controller {

    public static void list() {
        List<Talk> talks = Talk.findAllValidatedOn(ConferenceEvent.CURRENT);
        renderJSON(talks, new TalkJsonSerializer());
    }
}
