package models.activity;

import models.LightningTalk;
import models.Vote;
import org.junit.*;

/**
 * Unit tests for {@link NewVoteActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class NewVoteActivityTest extends AbstractActivityTest {

    private LightningTalk createLT() {
        return new LightningTalk().save();
    }
    
    @Test
    public void vote() {
        
        LightningTalk lt = createLT();
        
        // No activity for the talk
        assertEquals(0, Activity.count("session = ?", lt));
        assertNull(Activity.find("session = ?", lt).first());
        
        // new negative vote pour the lt
        Vote v = new Vote(lt, member, false).save();
        
        // Stille no activity for the talk
        assertEquals(0, Activity.count("session = ?", lt));
        assertNull(Activity.find("session = ?", lt).first());
        
        // Positive vote
        v.value = true;
        v.save();
        
        // One activity for the session
        assertEquals(1, Activity.count("session = ?", lt));
        Activity a = Activity.find("session = ?", lt).first();
        assertActivity(a);
        assertTrue(a instanceof NewVoteActivity);
        NewVoteActivity nva = (NewVoteActivity) a;
        assertEquals(lt, nva.session);
        assertEquals(member, nva.member);
    }
}
