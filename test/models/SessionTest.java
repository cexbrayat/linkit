package models;

import java.util.Arrays;

import com.google.common.collect.Sets;
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
    public void findValidatedSessionsLinkedWith() {
        final Talk talk1 = new Talk().save();
        talk1.validate();
        final Session lightning2 = new LightningTalk().save();
        final Talk talkInvalid = new Talk().save();
        talkInvalid.unvalidate();

        final String interest1 = "interest1";
        final String interest2 = "interest2";
        assertEquals(0, Session.findValidatedLinkedWith(Interest.findOrCreateByName(interest1)).size());

        // Add interest now
        talk1.addInterest(interest1).addInterest(interest2).save();
        lightning2.addInterest(interest1).save();
        talkInvalid.addInterest(interest1).save();

        // Simple Check
        assertEquals(Arrays.asList(talk1, lightning2), Session.findValidatedLinkedWith(Interest.findByName(interest1)));
        assertEquals(Arrays.asList(talk1), Session.findValidatedLinkedWith(Interest.findByName(interest2)));
    }

    @Test
    public void findAllSessionsLinkedWith() {
        final Talk talk1 = new Talk().save();
        talk1.validate();
        final Session lightning2 = new LightningTalk().save();
        final Talk talkInvalid = new Talk().save();
        talkInvalid.unvalidate();

        final String interest1 = "interest1";
        final String interest2 = "interest2";
        assertEquals(0, Session.findAllLinkedWith(Interest.findOrCreateByName(interest1)).size());

        // Add interest now
        talk1.addInterest(interest1).addInterest(interest2).save();
        lightning2.addInterest(interest1).save();
        talkInvalid.addInterest(interest1).save();

        // Simple Check
        assertEquals(Sets.newHashSet(talk1, talkInvalid, lightning2), Sets.newHashSet(Session.findAllLinkedWith(Interest.findByName(interest1))));
        assertEquals(Arrays.asList(talk1), Session.findAllLinkedWith(Interest.findByName(interest2)));
    }

    @Test public void lookByValid() {
        final Session session = Session.all().first();
        // Ensure session valid
        session.valid = true;
        session.save();
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
    
    @Test public void lookByNonValid() {
        final Session session = Session.all().first();
        // Ensure session non valid
        session.valid = false;
        session.save();
        final Member ced = Member.findByLogin("ced");
        final long nbLooks = session.getNbLooks();

        // Even if a non valid session is displayed, it is not counted
        session.lookedBy(ced);
        session.lookedBy(null);
        assertEquals(nbLooks, session.getNbLooks());
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
