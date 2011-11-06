package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link Sponsor}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SponsorTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }
        
    @Test public void load() {
        final String sponsorLogin = "sponsor";
        
        Sponsor sponsor = Sponsor.findByLogin(sponsorLogin);
        assertNotNull(sponsor);
        assertNotNull(sponsor.badges);
        assertTrue(sponsor.badges.contains(Badge.Sponsor));
    }

    @Test public void create() {
        Sponsor s = new Sponsor("toto", new LinkItAccount("password"));
        assertNotNull(s.badges);
        assertTrue(s.badges.contains(Badge.Sponsor));
        s.save();
    }
}
