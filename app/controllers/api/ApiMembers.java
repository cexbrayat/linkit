package controllers.api;

import com.google.gson.JsonSerializer;
import models.*;

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

    private static JsonSerializer TALK_SERIALIZERS[] = new JsonSerializer[] {
            new TalkJsonSerializer(true),
            new LightningTalkJsonSerializer(true),
    };

    public static void speakers(boolean details) {
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
        member(member, details);
    }

    public static void memberByLogin(String login, boolean details) {
        Member member = Member.findByLogin(login);
        member(member, details);
    }

    private static void member(Member member, boolean details) {
        notFoundIfNull(member);
        renderJSON(member, getSerializers(details));
    }

    private static JsonSerializer[] getSerializers(boolean details) {
        return details ? DETAILED_MEMBER_SERIALIZERS : MEMBER_SERIALIZERS;
    }

    public static void favorites(long memberId) {
        Member member = Member.findById(memberId);
        favorites(member);
    }

    public static void favoritesByLogin(String memberLogin) {
        Member member = Member.findByLogin(memberLogin);
        favorites(member);
    }

    private static void favorites(Member member) {
        notFoundIfNull(member);
        List<Talk> favorites = Vote.findFavoriteTalksByMemberOn(member, ConferenceEvent.CURRENT);
        renderJSON(favorites, TALK_SERIALIZERS);
    }
}
