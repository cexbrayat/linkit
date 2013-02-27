package controllers;

import java.util.List;

import models.ConferenceEvent;
import models.Role;
import models.Talk;
import static play.modules.pdf.PDF.renderPDF;
import play.mvc.With;

@With(SecureLinkIt.class)
@Check(Role.ADMIN_SESSION)
public class Exports extends PageController {

    public static void index() {
        render();
    }

    public static void exportSessions() {
        List<Talk> talks = Talk.find("valid = false and event = ? order by track, title", ConferenceEvent.CURRENT).fetch();
        renderPDF(talks);
    }
}