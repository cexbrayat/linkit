package controllers.api;

import com.google.gson.JsonSerializer;
import models.*;
import play.mvc.Controller;

import java.util.List;

public class ApiMembers extends JsonpController {

    private static JsonSerializer MEMBER_SERIALIZERS[] = new JsonSerializer[] {
        new MemberJsonSerializer(), new SponsorJsonSerializer(), new StaffJsonSerializer()
    };

    public static void speakers() {
        List<Member> speakers = Talk.findAllSpeakersOn(ConferenceEvent.CURRENT);
        renderJSON(speakers, MEMBER_SERIALIZERS);
    }

    public static void sponsors() {
        List<Sponsor> sponsors = Sponsor.findOn(ConferenceEvent.CURRENT);
        renderJSON(sponsors, MEMBER_SERIALIZERS);
    }
    public static void staff() {
        List<Staff> staff = Staff.findAll();
        renderJSON(staff, MEMBER_SERIALIZERS);
    }

    public static void members() {
        List<Member> members = Member.findAll();
        renderJSON(members, MEMBER_SERIALIZERS);
    }

    public static void member(long id) {
        Member member = Member.findById(id);
        notFoundIfNull(member);
        renderJSON(member, MEMBER_SERIALIZERS);
    }
}
