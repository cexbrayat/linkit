package models;

import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link Speaker}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SpeakerTest extends UnitTest {
    
    @Test public void newBadge() {
        Speaker s = new Speaker("toto", new LinkItAccount("password"));
        assertNotNull(s.badges);
        assertTrue(s.badges.contains(Badge.Speaker));
    }
    
}
