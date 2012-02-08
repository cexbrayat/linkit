package models.activity;

import models.Talk;
import org.junit.*;

/**
 * Unit tests for {@link UpdateSessionActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class NewTalkActivityTest extends AbstractActivityTest {
    
    @Test
    public void validateSession() {
        
        Talk t = createTalk("test");
        
        // No activity for the talk
        assertEquals(0, Activity.count("session = ?", t));
        assertNull(Activity.find("session = ?", t).first());
        
        t.validate();
        
        // One activity for the session
        assertEquals(1, Activity.count("session = ?", t));
        Activity a = Activity.find("session = ?", t).first();
        assertActivity(a);
        assertTrue(a instanceof NewTalkActivity);
        NewTalkActivity nta = (NewTalkActivity) a;
        assertEquals(t, nta.session);
    }
}
