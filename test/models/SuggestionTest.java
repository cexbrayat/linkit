package models;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import java.util.Set;
import org.junit.*;

/**
 * Unit tests for {@link Suggestion} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SuggestionTest extends BaseDataUnitTest {

    @Test
    public void testFindMembersInterestedInAllOf() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();

        // Advanced Checks
        // Members inerested in ALL OF the Interests
        List<Interest> interests = new ArrayList<Interest>();
        interests.add(Interest.findOrCreateByName("Java"));
        assertEquals(2, Suggestion.findMembersInterestedInAllOf(interests).size());
        interests.add(Interest.findOrCreateByName("Hadoop"));
        assertEquals(1, Suggestion.findMembersInterestedInAllOf(interests).size());
        interests.add(Interest.findOrCreateByName("TDD"));
        assertEquals(0, Suggestion.findMembersInterestedInAllOf(interests).size());
    }
    
    @Test
    public void testSuggestedMembersNoLinks() {
        Member member1 = createMember("member1");
        member1.addInterest("i1").addInterest("i2").addInterest("i3").save();
        Member member2 = createMember("member2");
        member2.addInterest("i2").addInterest("i3").addInterest("i4").save();
        Member member3 = createMember("member3");
        member3.addInterest("i4").addInterest("i5").addInterest("i6").save();
        
        assertEquals(Arrays.asList(member1, member3), Suggestion.suggestedMembersFor(member2, 10));
    }
    
    @Test
    public void testSuggestedMembersWithLinks() {
        Member member1 = createMember("member1");
        member1.addInterest("i1").addInterest("i2").save();
        Member member2 = createMember("member2");
        member2.addInterest("i1").save();
        Member member3 = createMember("member3");
        member3.addInterest("i1").save();
        Member member4 = createMember("member4");
        member4.addInterest("i1").addInterest("i2").save();
        Member member5 = createMember("member5");
        member5.addInterest("i1").save();

        member1.addLink(member3);
        member1.addLink(member5);
        member1.save();
        
        assertEquals(Arrays.asList(member4, member2), Suggestion.suggestedMembersFor(member1, 10));
    }
    
    @Test
    public void testSuggestedMembersLimit() {
        Member member1 = createMember("member1");
        member1.addInterest("i").save();
        Member member2 = createMember("member2");
        member2.addInterest("i").save();
        Member member3 = createMember("member3");
        member3.addInterest("i").save();
        Member member4 = createMember("member4");
        member4.addInterest("i").save();
        
        List<Member> suggested = Suggestion.suggestedMembersFor(member2, 2);
        assertEquals(2, suggested.size());
    }
    
    @Test
    public void testSuggestedMembersWithNoInterests() {
        // Member with no interests
        Member notinterested = new Member("toto").save();
        assertEquals(0, Suggestion.suggestedMembersFor(notinterested, 100).size());
    }

    @Test
    public void findSessionsAbout() {
        final Talk s1 = createTalk("session1");
        final Session s2 = createLightningTalk("session2");
        final Talk s3 = createTalk("session3 invalid");
        s3.unvalidate();
        final Talk s4 = createTalk("session4");

        final String commonInterest = "common";
        s1.addInterest(commonInterest).addInterest("nimp").save();
        s2.addInterests("nimp", commonInterest).save();
        s3.addInterest(commonInterest).save();
        s4.addInterest("nimp");
        
        List<Interest> interests = new ArrayList<Interest>();
        interests.add(Interest.findOrCreateByName("toto"));
        assertEquals(Collections.emptyList(), Suggestion.findSessionsAbout(interests));
        interests.add(Interest.findOrCreateByName(commonInterest));
        Set<Session> expectedSessions = Sets.newHashSet(s1,s2);
        assertEquals(2, Suggestion.findSessionsAbout(interests).size());
        assertTrue(Suggestion.findSessionsAbout(interests).containsAll(expectedSessions));
        interests.add(Interest.findOrCreateByName("tata"));
        assertEquals(2, Suggestion.findSessionsAbout(interests).size());
        assertTrue(Suggestion.findSessionsAbout(interests).containsAll(expectedSessions));
    }
    
    private static Talk createTalk(String text) {
        Talk s = new Talk();
        s.title = text;
        s.summary = text;
        s.description = text;
        s.track = Track.Agility;
        s.validate();
        return s;
    }
    
    private static LightningTalk createLightningTalk(String text) {
        LightningTalk t = new LightningTalk();
        t.title = text;
        t.summary = text;
        t.description = text;
        return t.save();
    }
    
    @Test
    public void suggestedSessionsFor() {
        final Talk s1 = createTalk("session1");
        final Session s2 = createLightningTalk("session2");
        final Talk s3 = createTalk("session3");
        final Talk s4 = createTalk("session4 invalide");
        s4.unvalidate();

        final String commonInterest1 = "TOTO";
        final String commonInterest2 = "TATA";
        s1.addInterest(commonInterest1).addInterest("TUTU").save();
        s2.addInterest(commonInterest1).addInterest(commonInterest2).save();
        s3.addInterest("nimp").addInterest("debile").save();
        s4.addInterest(commonInterest1).addInterest(commonInterest2).save();

        final Member member = Member.all().first();
        member.addInterest(commonInterest1).addInterest(commonInterest2).save();
 
        assertEquals(Arrays.asList(s2, s1), Suggestion.suggestedSessionsFor(member, 10));
    }
    
    @Test
    public void suggestedSessionsForLimit() {
        final Session s1 = createTalk("session1");
        final Session s2 = createLightningTalk("session2");
        final Session s3 = createTalk("session3");
        final Session s4 = createTalk("session4");
        final Session s5 = createLightningTalk("session5");
        final Session s6 = createTalk("session6");

        final String interest = "i1";
        s1.addInterest(interest).save();
        s2.addInterest(interest).save();
        s3.addInterest(interest).save();
        s4.addInterest(interest).save();
        s5.addInterest(interest).save();
        s5.addInterest(interest).save();

        final Member member = createMember("toto");
        member.addInterest(interest).save();
 
        // Limit 2
        assertEquals(2, Suggestion.suggestedSessionsFor(member, 2).size());
    }
    
    private static Member createMember(String login) {
        return new Member(login).save();
    }
    
    @Test
    public void missingBadgesFor() {
        Member member = createMember("toto");
        member.addBadge(Badge.Brave);
        member.addBadge(Badge.FiveDaysInARow);
        assertEquals(EnumSet.complementOf(EnumSet.of(Badge.Brave, Badge.FiveDaysInARow, Badge.Sponsor, Badge.Staff)), Suggestion.missingBadgesFor(member));
    }
    
    @Test
    public void missingBadgesForMemberWithNoBadge() {
        Member member = createMember("toto");
        assertEquals(Badge.EarnableBadges, Suggestion.missingBadgesFor(member));
    }
}
