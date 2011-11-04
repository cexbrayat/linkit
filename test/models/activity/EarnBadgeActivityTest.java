package models.activity;

import models.Badge;
import models.Member;
import org.junit.*;

/**
 * Unit tests for {@link EarnBadgeActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class EarnBadgeActivityTest extends AbstractActivityTest {

    @Test
    @Ignore // FIXME EarnBadgeActivity
    public void addBadge() {
        Member bob = Member.findByLogin("bob");
        
        // No activity for bob
        assertNull(Activity.find("select a from Activity a where a.member = ?", bob).first());
        
        bob.addBadge(Badge.Attendee);
        bob.save();
        
        // One activity for bob
        Activity a = Activity.find("select a from Activity a where a.member = ?", bob).first();
        assertActivity(a);
        assertTrue(a instanceof EarnBadgeActivity);
        EarnBadgeActivity eba = (EarnBadgeActivity) a;
        assertEquals(Member.findByLogin("bob"), eba.member);
        assertEquals(Badge.Attendee, eba.badge);
    }
}
