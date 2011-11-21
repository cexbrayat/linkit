package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link TwitterOAuthAccount} 
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TwitterOAuthAccountTest extends UnitTest {

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
    public void testInitMemberProfileNull() {
        new TwitterOAuthAccount(null, null).initMemberProfile();
        // Should not fail even if Account.member == null
    }

    @Test
    public void testInitMemberProfileEmpty() {
        final TwitterOAuthAccount ta = new TwitterOAuthAccount(null, null);
        ta.name = "Jean Dupont";
        ta.screenName = "jean_dupont";

        Member m = new Member("login", ta.getAccount());
        ta.initMemberProfile();

        assertEquals(ta.name, m.displayName);
        assertEquals(ta.screenName, m.twitterName);
    }

    @Test
    public void testInitMemberProfileNotEmpty() {
        final TwitterOAuthAccount ta = new TwitterOAuthAccount(null, null);
        ta.name = "Jean Dupont";
        ta.screenName = "jean_dupont";

        final String originalDisplayName = "MaDescription";
        final String originalTwitterName = "MonTwitter";
        Member m = new Member("login", ta.getAccount());
        m.displayName = originalDisplayName;
        m.twitterName = originalTwitterName;
        ta.initMemberProfile();

        // Member profile not modified
        assertEquals(originalDisplayName, m.displayName);
        assertEquals(originalTwitterName, m.twitterName);
    }

    @Test
    public void findCorrespondingMemberOK() {
        final TwitterOAuthAccount ta = new TwitterOAuthAccount(null, null);
        ta.screenName = "cedric_exbrayat";
        assertEquals(Member.findByLogin("ced"), ta.findCorrespondingMember());
    }

    @Test
    public void findCorrespondingMemberNotFound() {
        final TwitterOAuthAccount ta = new TwitterOAuthAccount(null, null);
        ta.screenName = "MonTwitter";
        assertNull(ta.findCorrespondingMember());
    }
}
