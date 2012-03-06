package models;

import org.junit.Test;

/**
 * Unit tests for {@link Sponsor}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SponsorTest extends BaseDataUnitTest {
        
    @Test public void load() {
        final String sponsorLogin = "sponsor";
        
        Sponsor sponsor = Sponsor.findByLogin(sponsorLogin);
        assertNotNull(sponsor);
        assertNotNull(sponsor.badges);
        assertTrue(sponsor.badges.contains(Badge.Sponsor));
    }

    @Test public void create() {
        Sponsor s = new Sponsor("toto");
        assertNotNull(s.badges);
        assertTrue(s.badges.contains(Badge.Sponsor));
        s.save();
    }

    private Sponsor createSponsor(final String login, final String firstName, final String lastName) {
        Sponsor s = new Sponsor(login);
        s.firstname = firstName;
        s.lastname = lastName;
        return s;
    }
    
    @Test public void testToString() {
        assertEquals("lastName", createSponsor("toto", null, "lastName").toString());
        assertEquals("lastName", createSponsor("toto", "", "lastName").toString());
        assertEquals("firstName", createSponsor("toto", "firstName", null).toString());
        assertEquals("firstName", createSponsor("toto", "firstName", "").toString());
        assertEquals("firstName lastName", createSponsor("toto", "firstName", "lastName").toString());
    }
}
