
import java.util.List;
import java.util.Map;

import controllers.Security;

import org.junit.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

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
    public void connect() {
        assertFalse(Security.authenticate("bob", "bob"));
        assertFalse(Security.authenticate("ced", "bob"));
        assertTrue(Security.authenticate("bob", "secret"));
    }

    @Test
    public void addLink() {
        Member bob = Member.find("byLogin", "bob").first();
        assertEquals(0, bob.links.size());
        Member.addLink("bob", "ced");
        assertEquals(1, bob.links.size());
    }

    @Test
    public void isLinkedTo() {
        Member bob = Member.find("byLogin", "bob").first();
        assertEquals(0, bob.links.size());
        assertEquals(false, Member.isLinkedTo("bob", "ced"));
        Member.addLink("bob", "ced");
        assertEquals(1, bob.links.size());
        assertEquals(true, Member.isLinkedTo("bob", "ced"));
    }

    @Test
    public void testInterests() {
        Member bob = Member.find("byLogin", "bob").first();
        Member ced = Member.find("byLogin", "ced").first();

        // Well
        assertEquals(0, Member.findMembersInterestedBy("Java").size());

        // Add interest now
        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();

        // Simple Check
        assertEquals(2, Member.findMembersInterestedBy("Java").size());
        assertEquals(1, Member.findMembersInterestedBy("TDD").size());
        assertEquals(1, Member.findMembersInterestedBy("Hadoop").size());

        // Advanced Check
        assertEquals(2, Member.findMembersInterestedBy("Java").size());
        assertEquals(1, Member.findMembersInterestedBy("Java", "Hadoop").size());
        assertEquals(1, Member.findMembersInterestedBy("Java", "TDD").size());
        assertEquals(0, Member.findMembersInterestedBy("Hadoop", "TDD").size());
        assertEquals(0, Member.findMembersInterestedBy("Java", "Hadoop", "TDD").size());

        // Check Interests Cloud
        // Be careful to the alphabetical order!
        List<Map> cloud = Interest.getCloud();
        assertEquals(
                "[{interest=Hadoop, pound=1}, {interest=Java, pound=2}, {interest=TDD, pound=1}]",
                cloud.toString());

    }
}
