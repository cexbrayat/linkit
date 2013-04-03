package controllers.api;

import com.google.gson.JsonSerializer;
import models.*;
import play.mvc.Controller;

import java.util.List;

public class ApiMembers extends JsonpController {

    private static JsonSerializer MEMBER_SERIALIZERS[] = new JsonSerializer[] {
            new MemberJsonSerializer(false),
            new SponsorJsonSerializer(false),
            new StaffJsonSerializer(false),
            new InterestJsonSerializer(),
            new SharedLinkJsonSerializer()
    };

    private static JsonSerializer DETAILED_MEMBER_SERIALIZERS[] = new JsonSerializer[] {
            new MemberJsonSerializer(true),
            new SponsorJsonSerializer(true),
            new StaffJsonSerializer(true),
            new InterestJsonSerializer(),
            new SharedLinkJsonSerializer()
    };

    public static void speakers(boolean details) {
    private static JsonSerializer TALK_SERIALIZERS[] = new JsonSerializer[] {
            new TalkJsonSerializer(true),
            new LightningTalkJsonSerializer(true),
    };

    public static void speakers() {
        List<Member> speakers = Talk.findAllSpeakersOn(ConferenceEvent.CURRENT);
        renderJSON(speakers, getSerializers(details));
    }

    public static void sponsors(boolean details) {
        List<Sponsor> sponsors = Sponsor.findOn(ConferenceEvent.CURRENT);
        renderJSON(sponsors, getSerializers(details));
    }
    public static void staff(boolean details) {
        List<Staff> staff = Staff.findAll();
        renderJSON(staff, getSerializers(details));
    }

    public static void members(boolean details) {
        List<Member> members = Member.findAll();
        renderJSON(members, getSerializers(details));
    }

    public static void member(long id, boolean details) {
        Member member = Member.findById(id);
        notFoundIfNull(member);
        renderJSON(member, getSerializers(details));
    }

    private static JsonSerializer[] getSerializers(boolean details) {
        return details ? DETAILED_MEMBER_SERIALIZERS : MEMBER_SERIALIZERS;
        renderJSON(member, MEMBER_SERIALIZERS);
    }

    public static void favorites(long memberId) {
        Member member = Member.findById(memberId);
        notFoundIfNull(member);
        List<Talk> favorites = Vote.findFavoriteTalksByMember(member);
        renderJSON(favorites, TALK_SERIALIZERS);
    }
}
