package models;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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
    public void testFindMembersInterestedInOneOf() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();
        
        // Members inerested in AT LEAST ONE OF the Interests        
        List<Interest> interests = new ArrayList<Interest>();
        interests.add(Interest.findOrCreateByName("Java"));
        assertEquals(2, Suggestion.findMembersInterestedInOneOf(interests).size());
        interests.add(Interest.findOrCreateByName("Hadoop"));
        assertEquals(2, Suggestion.findMembersInterestedInOneOf(interests).size());
        interests.add(Interest.findOrCreateByName("TDD"));
        assertEquals(2, Suggestion.findMembersInterestedInOneOf(interests).size());
    }
    
    @Test
    public void testSuggestedMembers() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        // Add interest now : Java is common
        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();
        
        // Ced has already Bob in his links, so we expect 0 suggested member
        assertEquals(0, Suggestion.suggestedMembersFor(ced).size());
        // Bos hasn't already Ced in his links, so we expect 1 suggested members
        assertEquals(1, Suggestion.suggestedMembersFor(bob).size());
        assertSame(ced, Suggestion.suggestedMembersFor(bob).iterator().next());
    }
    
    @Test
    public void testSuggestedMembersWithNoInterests() {
        // Member with no interests
        Member notinterested = new Member("toto").save();
        assertEquals(0, Suggestion.suggestedMembersFor(notinterested).size());
    }

    @Test
    public void findSessionsAbout() {
        final Session s1 = createSession("session1");
        final Session s2 = createSession("session2");

        s1.addInterest("Java").addInterest("Hadoop").save();
        s2.addInterest("TDD").addInterest("Java").save();
        
        List<Interest> interests = new ArrayList<Interest>();
        interests.add(Interest.findOrCreateByName("Java"));
        assertEquals(2, Suggestion.findSessionsAbout(interests).size());
        interests.add(Interest.findOrCreateByName("Hadoop"));
        assertEquals(2, Suggestion.findSessionsAbout(interests).size());
        interests.add(Interest.findOrCreateByName("TDD"));
        assertEquals(2, Suggestion.findSessionsAbout(interests).size());
    }
    
    private static Session createSession(String text) {
        Session s = new Session();
        s.title = text;
        s.summary = text;
        s.description = text;
        s.track = Track.Agility;
        return s.save();
    }
    
    @Test
    public void suggestedSessionsFor() {
        final Session s1 = createSession("session1");
        final Session s2 = createSession("session2");
        final Session s3 = createSession("session3");

        final String commonInterest1 = "TOTO";
        final String commonInterest2 = "TATA";
        s1.addInterest(commonInterest1).addInterest("TUTU").save();
        s2.addInterest(commonInterest1).addInterest(commonInterest2).save();
        s3.addInterest("nimp").addInterest("debile").save();

        final Member member = Member.all().first();
        member.addInterest(commonInterest1).addInterest(commonInterest2).save();
 
        assertEquals(Sets.newHashSet(s1, s2), Suggestion.suggestedSessionsFor(member));
    }
    
    private static Member createMember(String login) {
        return new Member(login).save();
    }
    
    @Test
    public void missingBadgesFor() {
        Member member = createMember("toto");
        member.addBadge(Badge.Brave);
        member.addBadge(Badge.FiveDaysInARow);
        assertEquals(EnumSet.complementOf(EnumSet.of(Badge.Brave, Badge.FiveDaysInARow)), Suggestion.missingBadgesFor(member));
    }
    
    @Test
    public void missingBadgesForMemberWithNoBadge() {
        Member member = createMember("toto");
        assertEquals(EnumSet.allOf(Badge.class), Suggestion.missingBadgesFor(member));
    }
}
