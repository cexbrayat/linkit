package models;

import org.junit.Test;

/**
 * Unit tests for {@link Speaker}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SpeakerTest extends BaseDataUnitTest {
        
    @Test public void load() {
        final String speakerLogin = "rguy";
        
        Speaker speaker = Speaker.findByLogin(speakerLogin);
        assertNotNull(speaker);
        assertNotNull(speaker.badges);
        assertTrue(speaker.badges.contains(Badge.Speaker));
    }

    @Test public void create() {
        Speaker s = new Speaker("toto");
        assertNotNull(s.badges);
        assertTrue(s.badges.contains(Badge.Speaker));
        s.save();
    }  
}
