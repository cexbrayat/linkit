package controllers.api;

import models.*;
import play.mvc.Controller;

import java.util.List;

public class ApiMembers extends Controller {

    public static void speakers() {
        List<Member> speakers = Talk.findAllSpeakersOn(ConferenceEvent.CURRENT);
        renderJSON(speakers, new MemberJsonSerializer());
    }

    public static void sponsors() {
        List<Sponsor> sponsors = Sponsor.findOn(ConferenceEvent.CURRENT);
        renderJSON(sponsors, new SponsorJsonSerializer());
    }
    public static void staff() {
        List<Staff> staff = Staff.findAll();
        renderJSON(staff, new MemberJsonSerializer(), new SponsorJsonSerializer(), new StaffJsonSerializer());
    }

    public static void members() {
        List<Member> members = Member.findAll();
        renderJSON(members, new MemberJsonSerializer(), new SponsorJsonSerializer(), new StaffJsonSerializer());
    }
}
