package models;

import java.util.Arrays;
import models.auth.GoogleOAuthAccount;
import models.auth.LinkItAccount;
import models.auth.TwitterOAuthAccount;
import org.apache.commons.lang.StringUtils;
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

    @Test public void findByLoginNull() {
        assertNull(Member.findByLogin(null));
    }

    @Test public void fetchForProfileOK() {
        Member ced = Member.fetchForProfile("ced");
        assertNotNull(ced);
        // FIXME How to close the current Hibernate session to ensure and test eager fetching of associated data?
        ced.links.toArray();
        ced.linkers.toArray();
        ced.badges.toArray();
        ced.interests.toArray();
        ced.accounts.toArray();
        ced.lightningTalks.toArray();
        ced.sharedLinks.toArray();
    }

    @Test public void fetchForProfileNotFound() {
        assertNull(Member.fetchForProfile("toto"));
    }
    
    @Test
    public void saveWithBigDescription() {
        Member bob = Member.findByLogin("bob");
        String description = StringUtils.rightPad("testwith4000char", 4000+3000, "a");
        bob.longDescription = description;
        assert(bob.longDescription.length()>4000);
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
   
    protected static Member createMember(final String login) {
        return new Member(login).save();
    }
    
    @Test
    public void testAddAccount() {
        final String login = "toto";
        Member toto = createMember(login);
        assertEquals(0, toto.accounts.size());

        // Ajout d'un compte Google
        final Account google1 = new GoogleAccount("id1");
        toto.addAccount(google1);
        toto.save();
        toto = Member.findByLogin(login);
        assertEquals(1, toto.accounts.size());

        // Ajout d'un second compte Google : pas de modification
        final Account google2 = new GoogleAccount("id2");
        toto.addAccount(google2);
        toto.save();
        toto = Member.findByLogin(login);
        assertEquals(1, toto.accounts.size());

        // Ajout d'un nouveau compte Twitter
        final Account twitter = new TwitterAccount("toto");
        toto.addAccount(twitter);
        toto.save();
        toto = Member.findByLogin(login);
        assertEquals(2, toto.accounts.size());
    }
   
    @Test
    public void testRemoveAccount() {
        final String login = "toto";
        Member toto = createMember(login);
        final GoogleAccount ga = new GoogleAccount("G+");
        final TwitterAccount ta = new TwitterAccount("twitter");
        toto.addAccount(ga);
        toto.addAccount(ta);
        toto.save();
        toto = Member.findByLogin(login);
        assertEquals(2, toto.accounts.size());
        
        toto.removeAccount(ga);
        toto.save();
 
        toto = Member.findByLogin(login);
        assertEquals(1, toto.accounts.size());
        assertSame(ta, toto.accounts.iterator().next());
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
    
    @Test public void lookBy() {
        final Member bob = Member.findByLogin("bob");
        final Member ced = Member.findByLogin("ced");
        final long nbLooks = bob.getNbLooks();
        bob.lookedBy(bob);
        assertEquals(nbLooks, bob.getNbLooks());
        bob.lookedBy(ced);
        assertEquals(nbLooks+1, bob.getNbLooks());
        bob.lookedBy(null);
        assertEquals(nbLooks+2, bob.getNbLooks());
    }
    
    @Test public void getAccount() {
        final Account googleAccount = new GoogleAccount("1234");
        final Member m = new Member("toto");
        m.addAccount(googleAccount);
        
        assertSame(googleAccount, m.getAccount(ProviderType.Google));
        assertSame(googleAccount, m.getGoogleAccount());
        assertNull(m.getAccount(ProviderType.Twitter));
        assertNull(m.getTwitterAccount());
    }
    
    @Test public void getAccountProviders() {
        final Member m = new Member("toto");
        m.addAccount(new GoogleAccount("1234"));
        
        // Preserver order of ProviderType.values(), and always LinkIt
        assertEquals(Arrays.asList(ProviderType.LinkIt, ProviderType.Google), m.getAccountProviders());
    }
    
    @Test public void getOrderedAccounts() {
        final Account twitterAccount = new TwitterAccount("twitter");
        final Account googleAccount = new GoogleAccount("1234");
        final Member m = new Member("toto");
        m.addAccount(googleAccount);
        m.addAccount(twitterAccount);
        
        // Preserver order of ProviderType.values()
        assertEquals(Arrays.asList(twitterAccount, googleAccount), m.getOrderedAccounts());
    }
    
    @Test public void delete() {
        Member member = createMember("member");
        // Some accounts
        member.addAccount(new GoogleAccount("1234"));
        member.addAccount(new TwitterAccount("@member"));
        member.register(new LinkItAccount("password"));
        member.register(new GoogleOAuthAccount("token", "secret"));
        member.register(new TwitterOAuthAccount("token", "secret"));
        // Some shared links
        member.addSharedLink(new SharedLink("Google", "http://www.google.com"));
        member.addSharedLink(new SharedLink("Yahoo", "http://www.yahoo.fr"));
        // Add some activities
        member.updateProfile();
        Member other = createMember("other");
        // Add links
        member.addLink(other);
        other.addLink(member);
        member.save();
        other.save();
        // Some comments
        final Session session = Session.all().first();
        session.addComment(new SessionComment(member, session, "commentaire"));
        final Article article = Article.all().first();
        article.addComment(new ArticleComment(member, article, "commentaire"));
        
        assertNotNull(member.delete());
        assertSame(other, Member.findById(other.id));
    }
}
