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
        
        // Non activity for the session
        assertNull(Activity.find("select a from Activity a where a.session = ?", s).first());
        
        s.summary = "Un nouveau résumé";
        s.save();
        
        // One activity for Bob
        Activity a = Activity.find("select a from Activity a where a.session = ?", s).first();
        assertActivity(a);
        assertTrue(a instanceof UpdateSessionActivity);
        UpdateSessionActivity usa = (UpdateSessionActivity) a;
        assertEquals(s, usa.session);
    }
}
