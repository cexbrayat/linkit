package models;

import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link GoogleAccount} 
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GoogleAccountTest extends UnitTest {
    
    @Test
    public void testInitMemberProfileNull() {
        new GoogleAccount(null, null).initMemberProfile();
    }
    
    @Test
    public void testInitMemberProfileOK() {
        final GoogleAccount ga = new GoogleAccount(null, null);
        ga.email = "toto@toto.com";
        ga.googleId = "G+";
        ga.givenName = "Jean";
        ga.familyName = "Dupont";
        ga.name = "Jean *Cule* Dupont";
        
        Member m = new Member("login", ga);
        ga.initMemberProfile();
        
        assertEquals(ga.googleId, m.googlePlusId);
        assertEquals(ga.email, m.email);
        assertEquals(ga.givenName, m.firstname);
        assertEquals(ga.familyName, m.lastname);
        assertEquals(ga.name, m.displayName);
    }
}
