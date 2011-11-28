package models.activity;

import models.Session;
import org.junit.*;

/**
 * Unit tests for {@link LookSessionActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LookSessionActivityTest extends AbstractActivityTest {

    @Test
    public void lookSession() {
        
        Session s = Session.all().first();
        
        // No activity for the session
        assertEquals(0, Activity.count("session = ?", s));
        assertNull(Activity.find("session = ?", s).first());
        
        s.lookedBy(member);
        
        // One activity for the session
        assertEquals(1, Activity.count("session = ?", s));
        Activity a = Activity.find("session = ?", s).first();
        assertActivity(a);
        assertTrue(a instanceof LookSessionActivity);
        LookSessionActivity lsa = (LookSessionActivity) a;
        assertEquals(s, lsa.session);
        assertEquals(member, lsa.member);
    }

    @Test
    public void lookSessionBySpeaker() {
        
        Session s = Session.all().first();
        
        // No activity for the session
        assertEquals(0, Activity.count("session = ?", s));
        assertNull(Activity.find("session = ?", s).first());
        
        // Session looked by one of speakers
        s.lookedBy(s.speakers.iterator().next());
        
        // Still no activity for the session
        assertEquals(0, Activity.count("session = ?", s));
        assertNull(Activity.find("session = ?", s).first());
    }

    @Test
    public void lookSessionByNull() {
        
        Session s = Session.all().first();
        
        // No activity for the session
        assertEquals(0, Activity.count("session = ?", s));
        assertNull(Activity.find("session = ?", s).first());
        
        // A not connected user looked at the session
        s.lookedBy(null);
        
        // Still no activity for the session
        assertEquals(0, Activity.count("session = ?", s));
        assertNull(Activity.find("session = ?", s).first());
    }
}
