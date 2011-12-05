package models.auth;

import models.BaseDataUnitTest;
import models.Member;
import models.TwitterAccount;
import org.junit.Test;

/**
 * Unit tests for {@link TwitterOAuthAccount} 
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TwitterOAuthAccountTest extends BaseDataUnitTest {

    @Test
    public void testInitMemberProfileNull() {
        new TwitterOAuthAccount(null, null).initMemberProfile();
        // Should not fail even if Account.member == null
    }

    @Test
    public void getOAuthLogin() {
        final TwitterOAuthAccount ta = new TwitterOAuthAccount(null, null);
        ta.screenName = "jean_dupont";
        assertEquals(ta.screenName, ta.getOAuthLogin());
    }
    
    @Test
    public void testInitMemberProfileEmpty() {
        final TwitterOAuthAccount ta = new TwitterOAuthAccount(null, null);
        ta.screenName = "jean_dupont";
        ta.name = "Jean Dupont";
        ta.member = new Member("login");

        ta.initMemberProfile();

        assertEquals(ta.screenName, ta.member.getTwitterAccount().screenName);
    }

    @Test
    public void testInitMemberProfileNotEmpty() {
        final TwitterOAuthAccount ta = new TwitterOAuthAccount(null, null);
        ta.screenName = "jean_dupont";
        ta.name = "Jean Dupont";
        ta.member = new Member("login");
        final String originalTwitterName = "MonTwitter";
        TwitterAccount originalTwitterAccount = new TwitterAccount(originalTwitterName);
        ta.member.addAccount(originalTwitterAccount);
        ta.initMemberProfile();

        // Member profile not modified
        assertEquals(originalTwitterName, ta.member.getTwitterAccount().screenName);
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
