package models.auth;

import models.BaseDataUnitTest;
import models.GoogleAccount;
import models.Member;
import org.junit.Test;

/**
 * Unit tests for {@link GoogleOAuthAccount} 
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GoogleOAuthAccountTest extends BaseDataUnitTest {

    @Test public void getOAuthLoginOK() {
        final GoogleOAuthAccount ta = new GoogleOAuthAccount(null, null);
        ta.email = "jean.dupont@gmail.com";
        assertEquals("jean.dupont", ta.getOAuthLogin());
    }

    @Test public void getOAuthLoginNotValidEmail() {
        final GoogleOAuthAccount ta = new GoogleOAuthAccount(null, null);
        ta.email = "jean.dupont";   // No @
        assertEquals("jean.dupont", ta.getOAuthLogin());
        ta.email = "jean.dupont@test@test.com";   // Several @
        assertEquals("jean.dupont", ta.getOAuthLogin());
    }

    @Test
    public void testInitMemberProfileNull() {
        new GoogleOAuthAccount(null, null).initMemberProfile();
        // Should not fail even if Account.member == null
    }

    @Test
    public void testInitMemberProfileEmpty() {
        final GoogleOAuthAccount ga = new GoogleOAuthAccount(null, null);
        ga.googleId = "G+";
        ga.email = "toto@toto.com";
        ga.givenName = "Jean";
        ga.familyName = "Dupont";
        ga.name = "Jean Dupont";
        ga.member = new Member("login");

        ga.initMemberProfile();

        assertEquals(ga.googleId, ga.member.getGoogleAccount().googleId);
        assertEquals(ga.email, ga.member.email);
        assertEquals(ga.givenName, ga.member.firstname);
        assertEquals(ga.familyName, ga.member.lastname);
    }

    @Test
    public void testInitMemberProfileNotEmpty() {
        final GoogleOAuthAccount ga = new GoogleOAuthAccount(null, null);
        ga.googleId = "G+";
        ga.email = "toto@toto.com";
        ga.givenName = "Jean";
        ga.familyName = "Dupont";
        ga.name = "Jean Dupont";
        ga.member = new Member("login");

        final String originalGooglePlusId = "123";
        final String originalEmail = "mon@email.ch";
        final String originalFirstName = "Jade";
        final String originalLastName = "Aure";
        ga.member.addAccount(new GoogleAccount(originalGooglePlusId));
        ga.member.email = originalEmail;
        ga.member.firstname = originalFirstName;
        ga.member.lastname = originalLastName;

        ga.initMemberProfile();
        assertEquals(originalGooglePlusId, ga.member.getGoogleAccount().googleId);
        assertEquals(originalEmail, ga.member.email);
        assertEquals(originalFirstName, ga.member.firstname);
        assertEquals(originalLastName, ga.member.lastname);
    }

    @Test
    public void findCorrespondingMemberOK() {
        final GoogleOAuthAccount ga = new GoogleOAuthAccount(null, null);
        ga.email = "bob@gmail.com";
        assertEquals(Member.findByLogin("bob"), ga.findCorrespondingMember());
    }

    @Test
    public void findCorrespondingMemberNotFound() {
        final GoogleOAuthAccount ga = new GoogleOAuthAccount(null, null);
        ga.email = "toto@toto.com";
        assertNull(ga.findCorrespondingMember());
    }
}
