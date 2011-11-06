package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link Speaker}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SpeakerTest extends UnitTest {

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
        final String speakerLogin = "rguy";
        
        Speaker speaker = Speaker.findByLogin(speakerLogin);
        assertNotNull(speaker);
        assertNotNull(speaker.badges);
        assertTrue(speaker.badges.contains(Badge.Speaker));
    }

    @Test public void create() {
        Speaker s = new Speaker("toto", new LinkItAccount("password"));
        assertNotNull(s.badges);
        assertTrue(s.badges.contains(Badge.Speaker));
        s.save();
    }  
}
