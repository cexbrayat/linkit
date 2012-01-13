package models.activity;

import models.LightningTalk;
import models.Talk;
import org.junit.*;

/**
 * Unit tests for {@link UpdateSessionActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class UpdateSessionActivityTest extends AbstractActivityTest {

    private Talk createTalk() {
        return new Talk().save();
    }

    private LightningTalk createLightningTalk() {
        return new LightningTalk().save();
    }
    
    @Test
    public void updateSessionTalk() {
        
        Talk t = createTalk();
        
        // No activity for the talk
        assertEquals(0, Activity.count("session = ?", t));
        assertNull(Activity.find("session = ?", t).first());
        
        t.summary = "Un nouveau résumé";
        t.update();
        
        // Still no activity for the unvalidated talk
        assertEquals(0, Activity.count("session = ?", t));
        assertNull(Activity.find("session = ?", t).first());
        
        t.valid=true;
        t.summary = "Un nouveau nouveau résumé";
        t.update();
        
        // One activity for the session
        assertEquals(1, Activity.count("session = ?", t));
        Activity a = Activity.find("session = ?", t).first();
        assertActivity(a);
        assertTrue(a instanceof UpdateSessionActivity);
        UpdateSessionActivity usa = (UpdateSessionActivity) a;
        assertEquals(t, usa.session);
    }
    
    @Test
    public void updateSessionLightningTalk() {
        
        LightningTalk lt = createLightningTalk();
        
        // No activity for the LT
        assertEquals(0, Activity.count("session = ?", lt));
        assertNull(Activity.find("session = ?", lt).first());
        
        lt.summary = "Un nouveau résumé";
        lt.update();
        
        // One activity for the LT
        assertEquals(1, Activity.count("session = ?", lt));
        Activity a = Activity.find("session = ?", lt).first();
        assertActivity(a);
        assertTrue(a instanceof UpdateSessionActivity);
        UpdateSessionActivity usa = (UpdateSessionActivity) a;
        assertEquals(lt, usa.session);
    }
}
