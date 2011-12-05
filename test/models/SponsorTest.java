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
}
