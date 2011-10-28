package models;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import play.test.*;

/**
 * Unit tests for {@link Suggestion} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SuggestionTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void testFindMembersInterestedInAllOf() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();

        // Advanced Checks
        // Members inerested in ALL OF the Interests
        List<Interest> interests_list1 = new ArrayList<Interest>();
        interests_list1.add(Interest.findOrCreateByName("Java"));
        assertEquals(2, Suggestion.findMembersInterestedInAllOf(interests_list1).size());
        interests_list1.add(Interest.findOrCreateByName("Hadoop"));
        assertEquals(1, Suggestion.findMembersInterestedInAllOf(interests_list1).size());
        interests_list1.add(Interest.findOrCreateByName("TDD"));
        assertEquals(0, Suggestion.findMembersInterestedInAllOf(interests_list1).size());
    }

    @Test
    public void testFindMembersInterestedInOneOf() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();
        
        // Members inerested in AT LEAST ONE OF the Interests        
        List<Interest> interests_list2 = new ArrayList<Interest>();
        interests_list2.add(Interest.findOrCreateByName("Java"));
        assertEquals(2, Suggestion.findMembersInterestedInOneOf(interests_list2).size());
        interests_list2.add(Interest.findOrCreateByName("Hadoop"));
        assertEquals(2, Suggestion.findMembersInterestedInOneOf(interests_list2).size());
        interests_list2.add(Interest.findOrCreateByName("TDD"));
        assertEquals(2, Suggestion.findMembersInterestedInOneOf(interests_list2).size());
    }
    
    @Test
    public void testSuggestedMembers() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        // Add interest now : Java is common
        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();
        
        // Ced has already Bob in his links, so we except 0 suggested member
        assertEquals(0, Suggestion.suggestedMembersFor(ced).size());
        // Bos hasn't already Ced in his links, so we except 1 suggested members
        assertEquals(1, Suggestion.suggestedMembersFor(bob).size());
    }
    
    @Test
    public void testSuggestedMembersWithNoInterests() {
        // Member with no interests
        Member notinterested = new Member("toto", new LinkItAccount("password"));
        assertEquals(0, Suggestion.suggestedMembersFor(notinterested).size());
    }
}
