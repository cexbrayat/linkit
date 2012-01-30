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

    private Talk createTalk(Member... speakers) {
        Talk t = new Talk();
        for (Member s : speakers) {
            t.addSpeaker(s);
        }
        return t.save();
    }
    
    @Test public void recents() {
        assertNotNull(Talk.recents(1, 10));
    }

    @Test public void countSpeakers() {
        long initialNbSpeakers = Talk.countSpeakers();
        
        final Member speaker1 = createMember("speaker1");
        final Member speaker2 = createMember("speaker2");
        
        // add new invalid talk
        Talk t = createTalk(speaker1, speaker2);
        
        assertEquals(initialNbSpeakers, Talk.countSpeakers());
        
        // Validate talk
        t.validate();
        
        assertEquals(initialNbSpeakers+2, Talk.countSpeakers());
    }

    @Test public void findAllSpeakers() {
        assertNotNull(Talk.findAllSpeakers());
                
        final Member speaker = createMember("speaker");
        
        // add new invalid talk
        Talk t = createTalk(speaker);
        
        assertFalse(Talk.findAllSpeakers().contains(speaker));
        
        // Validate talk
        t.validate();
        
        assertTrue(Talk.findAllSpeakers().contains(speaker));
    }
    
    @Test public void findAllValidated() {
        final long initialCount = Talk.findAllValidated().size();

        // add new invalid talk
        Talk t = createTalk();
        
        assertEquals(initialCount, Talk.findAllValidated().size());
        
        // Validate talk
        t.validate();
        
        assertEquals(initialCount+1, Talk.findAllValidated().size());
        assertTrue(Talk.findAllValidated().contains(t));
    }
    
    @Test public void getShowUrl() {
        final Talk t = createTalk();
        assertNotNull(t.getShowUrl());
    }
        
    @Test public void lookBy() {
        final Talk talk = new Talk();
        assertEquals(0, talk.getNbLooks());
        
        // Any look is not counted if session not valid
        talk.valid = false;
        talk.lookedBy(null);
        assertEquals(0, talk.getNbLooks());

        talk.valid = true;
        talk.lookedBy(null);
        assertEquals(1, talk.getNbLooks());
        talk.lookedBy(null);
        assertEquals(2, talk.getNbLooks());

        talk.valid = false;
        talk.lookedBy(null);
        assertEquals(2, talk.getNbLooks());
    }
        
    @Test public void delete() {
        Member speaker1 = createMember("speaker1");
        Member speaker2 = createMember("speaker2");
        Talk lt = createTalk(speaker1, speaker2);
        // Some comments
        Member member1 = createMember("member1");
        Member member2 = createMember("member2");
        lt.addComment(new SessionComment(member1, lt, "Comentaire"));
        lt.addComment(new SessionComment(member2, lt, "Comentaire"));
        // Some votes
        new Vote(lt, member1, true).save();
        new Vote(lt, member2, false).save();
        lt.save();
        
        assertNotNull(lt.delete());
        assertNull(Talk.findById(lt.id));
    }

}
