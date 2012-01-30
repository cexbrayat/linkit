package models;

import org.apache.commons.lang.StringUtils;
import org.junit.*;

/**
 * Unit tests for {@link Session} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class SessionTest extends BaseDataUnitTest {
    
    @Test
    public void saveWithBigDescription() {
        Session session = new Talk();
        String description = StringUtils.leftPad("testwith4000char", 4000+3000, "a");
        session.description = description;
        assertTrue(session.description.length()>4000);
        session.save();
    }
    
        @Test
    public void testInterests() {
        Session session1 = new Talk();
        Session session2 = new Talk();

        // Well
        assertEquals(0, Session.findSessionsLinkedWith("Java").size());

        // Add interest now
        session1.addInterest("Java").addInterest("TDD").addInterest("Hadoop").save();
        session2.addInterest("Java").save();

        // Simple Check
        assertEquals(2, Session.findSessionsLinkedWith("Java").size());
        assertEquals(1, Session.findSessionsLinkedWith("TDD").size());
        assertEquals(1, Session.findSessionsLinkedWith("Hadoop").size());

    }
    
    @Test public void lookBy() {
        final Session session = Session.all().first();
        final Member speaker = session.speakers.iterator().next();
        final Member ced = Member.findByLogin("ced");
        final long nbLooks = session.getNbLooks();

        // If a speaker looks at his session, it is not counted
        session.lookedBy(speaker);
        assertEquals(nbLooks, session.getNbLooks());

        session.lookedBy(ced);
        assertEquals(nbLooks+1, session.getNbLooks());
        session.lookedBy(null);
        assertEquals(nbLooks+2, session.getNbLooks());
    }
    
    @Test public void hasSpeakerNull() {
        final Session session = Session.all().first();
        assertFalse(session.hasSpeaker(null));
    }
    
    @Test public void hasSpeakerBlank() {
        final Session session = Session.all().first();
        assertFalse(session.hasSpeaker(""));
    }
    
    @Test public void hasSpeakerOK() {
        final Session session = Session.all().first();
        final Member speaker = session.speakers.iterator().next();
        assertTrue(session.hasSpeaker(speaker.login));
    }
}
