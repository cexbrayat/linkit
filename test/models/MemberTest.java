package models;

import java.util.List;
import java.util.Map;

import org.h2.util.StringUtils;
import org.junit.*;
import play.test.*;

/**
 * Unit tests for {@link Member} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MemberTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }

    @Test public void findByLoginOK() {
        Member bob = Member.findByLogin("bob");
        assertNotNull(bob);
        assertEquals("Bob", bob.firstname);
    }

    @Test public void findByLoginNotFound() {
        assertNull(Member.findByLogin("toto"));
    }
    
    @Test
    public void saveWithBigDescription() {
        Member bob = Member.findByLogin("bob");
        String description = StringUtils.pad("testwith4000char", 4000, "a" , true);
        bob.description = description;
        bob.save();
    }
    
    @Test
    public void addLink() {
        Member bob = Member.findByLogin("bob");
        assertEquals(0, bob.links.size());
        Member.addLink("bob", "ced");
        assertEquals(1, bob.links.size());
    }

    @Test
    public void isLinkedTo() {
        Member bob = Member.findByLogin("bob");
        assertEquals(0, bob.links.size());
        assertEquals(false, Member.isLinkedTo("bob", "ced"));
        Member.addLink("bob", "ced");
        assertEquals(1, bob.links.size());
        assertEquals(true, Member.isLinkedTo("bob", "ced"));
    }

    @Test
    public void testInterests() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

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
    
    @Test
    public void testAddAccount() {
        Member bob = Member.findByLogin("bob");
        assertEquals(1, bob.accounts.size());

        // Ajout d'un compte Google
        final Account google1 = new GoogleAccount("ABC", "DEF");
        bob.addAccount(google1);
        bob.save();
        bob = Member.findByLogin("bob");
        assertEquals(2, bob.accounts.size());

        // Ajout d'un second compte Google : pas de modification
        final Account google2 = new GoogleAccount("GHI", "JKL");
        bob.addAccount(google2);
        bob.save();
        bob = Member.findByLogin("bob");
        assertEquals(2, bob.accounts.size());

        // Ajout d'un nouveau compte Twitter
        final Account twitter = new TwitterAccount("ABC", "DEF");
        bob.addAccount(twitter);
        bob.save();
        bob = Member.findByLogin("bob");
        assertEquals(3, bob.accounts.size());
    }
}
