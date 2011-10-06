package models.activity;

import models.Session;
import org.junit.*;

/**
 * Unit tests for {@link UpdateSessionActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class UpdateSessionActivityTest extends AbstractActivityTest {

    @Test
    public void updateSession() {
        
        Session s = Session.all().first();
        
        // No activity for the session
        assertEquals(0, Activity.count("session = ?", s));
        assertNull(Activity.find("session = ?", s).first());
        
        s.summary = "Un nouveau résumé";
        s.update();
        
        // One activity for the session
        assertEquals(1, Activity.count("session = ?", s));
        Activity a = Activity.find("session = ?", s).first();
        assertActivity(a);
        assertTrue(a instanceof UpdateSessionActivity);
        UpdateSessionActivity usa = (UpdateSessionActivity) a;
        assertEquals(s, usa.session);
    }
}
