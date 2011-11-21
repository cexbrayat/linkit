package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link GoogleOAuthAccount} 
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GoogleOAuthAccountTest extends UnitTest {

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
        new GoogleOAuthAccount(null, null).initMemberProfile();
        // Should not fail even if Account.member == null
    }

    @Test
    public void testInitMemberProfileEmpty() {
        final GoogleOAuthAccount ga = new GoogleOAuthAccount(null, null);
        ga.email = "toto@toto.com";
        ga.googleId = "G+";
        ga.givenName = "Jean";
        ga.familyName = "Dupont";
        ga.name = "Jean *Cule* Dupont";

        Member m = new Member("login", ga.getAccount());
        ga.initMemberProfile();

        assertEquals(ga.googleId, m.googlePlusId);
        assertEquals(ga.email, m.email);
        assertEquals(ga.givenName, m.firstname);
        assertEquals(ga.familyName, m.lastname);
        assertEquals(ga.name, m.displayName);
    }

    @Test
    public void testInitMemberProfileNotEmpty() {
        final GoogleOAuthAccount ga = new GoogleOAuthAccount(null, null);
        ga.email = "toto@toto.com";
        ga.googleId = "G+";
        ga.givenName = "Jean";
        ga.familyName = "Dupont";
        ga.name = "Jean *Cule* Dupont";

        final String originalGooglePlusId = "123";
        final String originalEmail = "mon@email.ch";
        final String originalFirstName = "Jade";
        final String originalLastName = "Aure";
        final String originalDisplayName = "Jade Aure";
        Member m = new Member("login", ga.getAccount());
        m.googlePlusId = originalGooglePlusId;
        m.email = originalEmail;
        m.firstname = originalFirstName;
        m.lastname = originalLastName;
        m.displayName = originalDisplayName;

        ga.initMemberProfile();
        assertEquals(originalGooglePlusId, m.googlePlusId);
        assertEquals(originalEmail, m.email);
        assertEquals(originalFirstName, m.firstname);
        assertEquals(originalLastName, m.lastname);
        assertEquals(originalDisplayName, m.displayName);
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
