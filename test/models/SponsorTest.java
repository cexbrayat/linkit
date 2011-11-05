package models;

import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link Sponsor}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SponsorTest extends UnitTest {
    
    @Test public void newBadge() {
        Sponsor s = new Sponsor("toto", new LinkItAccount("password"));
        assertNotNull(s.badges);
        assertTrue(s.badges.contains(Badge.Sponsor));
    }
    
}
