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
        List<Talk> talks = Talk.find("valid = false and event = ? order by id", ConferenceEvent.CURRENT).fetch();
        render(talks);
    }

    public static void exportSessionsCSV() {
        List<Session> sessions = Talk.find("event = ? order by id", ConferenceEvent.CURRENT).fetch();
        renderTemplate("Exports/sessions.csv", sessions);
    }

    public static void exportSpeakers() {
        List<Member> members = Talk.findAllSpeakers();
        renderTemplate("Exports/members.csv", members);
    }

    public static void exportFailedSpeakers() {
        List<Member> members = Talk.findFailedSpeakersOn(ConferenceEvent.CURRENT);
        renderTemplate("Exports/members.csv", members);
    }

    public static void exportSpeakersWorkshop() {
        List<Member> members = Talk.findAllSpeakersOf(TalkFormat.Workshop);
        renderTemplate("Exports/members.csv", members);
    }

    public static void exportSpeakersLightningTalk() {
        List<Member> members = LightningTalk.findAllSpeakersOn(ConferenceEvent.CURRENT);
        renderTemplate("Exports/members.csv", members);
    }

    public static void exportMembers() {
        List<Member> members = Member.find("order by lastname").fetch();
        renderTemplate("Exports/members.csv", members);
    }
}