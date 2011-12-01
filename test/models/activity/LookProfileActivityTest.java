package models.activity;

import models.Member;
import org.junit.*;

/**
 * Unit tests for {@link LookProfileActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LookProfileActivityTest extends AbstractActivityTest {

    @Test
    public void lookProfile() {
        
        // Non activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());

        // Our member looked at an other member profile
        final Member other = createMember("other");
        other.lookedBy(member);
        
        // One activity for member
        Activity a = Activity.find("select a from Activity a where a.member = ?", member).first();
        assertActivity(a);
        assertTrue(a instanceof LookProfileActivity);
        LookProfileActivity lpa = (LookProfileActivity) a;
        assertEquals(member, lpa.member);
        assertEquals(other, lpa.other);
    }

    @Test
    public void lookProfileByOwner() {
        
        // No activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());

        // Our member looked at his own profile
        member.lookedBy(member);
        
        // Still no activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());
    }

    @Test
    public void lookProfileByNull() {
        
        // No activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());

        // A not connected user looked at member profile
        member.lookedBy(null);
        
        // Still no activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());
    }
}
