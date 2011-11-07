package models;


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

    @Test public void fetchForProfileOK() {
        Member ced = Member.fetchForProfile("ced");
        assertNotNull(ced);
        // FIXME How to close the current Hibernate session to ensure and test eager fetching of associated data?
        ced.links.toArray();
        ced.linkers.toArray();
        ced.badges.toArray();
        ced.interests.toArray();
    }

    @Test public void fetchForProfileNotFound() {
        assertNull(Member.fetchForProfile("toto"));
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
        final int originalLinksNb = bob.links.size();
        Member.addLink("bob", "ced");
        assertEquals(originalLinksNb+1, bob.links.size());
    }

    @Test
    public void isLinkedTo() {
        Member bob = Member.findByLogin("bob");
        assertFalse(bob.isLinkedTo("ced"));
        Member.addLink("bob", "ced");
        assertTrue(bob.isLinkedTo("ced"));
    }

    @Test
    public void hasForLinker() {
        Member ced = Member.findByLogin("ced");
        final int originalLinkersNb = ced.linkers.size();
        assertFalse(ced.hasForLinker("bob"));
        Member.addLink("bob", "ced");
        assertEquals(originalLinkersNb+1, ced.linkers.size());
        assertTrue(ced.hasForLinker("bob"));
    }

    @Test
    public void testFindMembersInterestedIn() {
        Member bob = Member.findByLogin("bob");
        Member ced = Member.findByLogin("ced");

        // Well
        assertEquals(0, Member.findMembersInterestedIn("Java").size());

        // Add interest now
        ced.addInterest("Java").addInterest("Hadoop").save();
        bob.addInterest("TDD").addInterest("Java").save();

        // Simple Checks
        assertEquals(2, Member.findMembersInterestedIn("Java").size());
        assertEquals(1, Member.findMembersInterestedIn("TDD").size());
        assertEquals(1, Member.findMembersInterestedIn("Hadoop").size());
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
        
    @Test public void addBadge() {
        final String login = "ced";
        final Badge addedBadge = Badge.Sponsor;
        
        Staff staffMember = Staff.findByLogin(login);
        assertNotNull(staffMember);
        
        assertNotNull(staffMember.badges);
        final int originalNbBadges = staffMember.badges.size();
        
        staffMember.addBadge(addedBadge);
        staffMember.save();
        
        staffMember = Staff.findByLogin(login);
        assertEquals(originalNbBadges+1, staffMember.badges.size());
        
        // Adding same badge twice : no consequences
        staffMember.addBadge(addedBadge);
        staffMember.save();
        staffMember = Staff.findByLogin(login);
        assertEquals(originalNbBadges+1, staffMember.badges.size());
    }
}
