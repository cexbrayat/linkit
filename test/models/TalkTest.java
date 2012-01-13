package models;

import org.junit.*;

/**
 * Unit tests for {@link Session} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class TalkTest extends BaseDataUnitTest {

    private Member createMember(String login) {
        return new Member(login).save();
    }
    
    @Test public void recents() {
        assertNotNull(Talk.recents(1, 10));
    }

    @Test public void countSpeakers() {
        long initialNbSpeakers = Talk.countSpeakers();
        
        final Member speaker1 = createMember("speaker1");
        final Member speaker2 = createMember("speaker2");
        
        // add new invalid talk
        Talk t = new Talk();
        t.addSpeaker(speaker1);
        t.addSpeaker(speaker2);
        t.save();
        
        assertEquals(initialNbSpeakers, Talk.countSpeakers());
        
        // Validate talk
        t.validate();
        
        assertEquals(initialNbSpeakers+2, Talk.countSpeakers());
    }

    @Test public void findAllSpeakers() {
        assertNotNull(Talk.findAllSpeakers());
                
        final Member speaker = createMember("speaker");
        
        // add new invalid talk
        Talk t = new Talk();
        t.addSpeaker(speaker);
        t.save();
        
        assertFalse(Talk.findAllSpeakers().contains(speaker));
        
        // Validate talk
        t.validate();
        
        assertTrue(Talk.findAllSpeakers().contains(speaker));
    }

    @Test public void countTalksByMember() {
        final Member speaker = createMember("speaker");
        assertEquals(0, Talk.countTalksByMember(speaker));

        // add 2 new invalid talks
        Talk t1 = new Talk();
        t1.addSpeaker(speaker);
        t1.save();
        Talk t2 = new Talk();
        t2.addSpeaker(speaker);
        t2.save();
        
        assertEquals(0, Talk.countTalksByMember(speaker));
        
        // Validate talks
        t1.valid = true;
        t1.validate();
        t2.valid = true;
        t2.validate();
        
        assertEquals(2, Talk.countTalksByMember(speaker));
    }
    
    @Test public void findAllValidated() {
        final long initialCount = Talk.findAllValidated().size();

        // add new invalid talk
        Talk t = new Talk();
        t.save();
        
        assertEquals(initialCount, Talk.findAllValidated().size());
        
        // Validate talk
        t.validate();
        
        assertEquals(initialCount+1, Talk.findAllValidated().size());
        assertTrue(Talk.findAllValidated().contains(t));
    }
}
