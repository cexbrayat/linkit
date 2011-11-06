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
    public void addBadge() {
        // No activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());
        
        member.addBadge(Badge.Attendee);
        // Second add for ensuring no activity duplication
        member.addBadge(Badge.Attendee);
        member.save();
        
        // One activity for member
        assertEquals(1l, Activity.count("from Activity a where a.member = ?", member));
        Activity a = Activity.find("select a from Activity a where a.member = ?", member).first();
        assertActivity(a);
        assertTrue(a instanceof EarnBadgeActivity);
        EarnBadgeActivity eba = (EarnBadgeActivity) a;
        assertEquals(member, eba.member);
        assertEquals(Badge.Attendee, eba.badge);
    }
}
