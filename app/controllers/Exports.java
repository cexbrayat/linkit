package controllers;

import models.*;
import play.mvc.With;

import java.util.List;

import static play.modules.pdf.PDF.renderPDF;

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

    public static void exportSpeakers() {
        List<Member> members = Talk.findAllSpeakers();
        renderTemplate("Exports/members.csv", members);
    }

    public static void exportSpeakersWorkshop() {
        List<Member> members = Talk.findAllSpeakersOf(TalkFormat.Workshop);
        renderTemplate("Exports/members.csv", members);
    }

    public static void exportMembers() {
        List<Member> members = Member.find("order by lastname").fetch();
        renderTemplate("Exports/members.csv", members);
    }
}