package models.activity;

import models.Member;
import org.junit.*;

/**
 * Unit tests for {@link LinkActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LinkActivityTest extends AbstractActivityTest {

    @Test
    public void addLink() {
        // Non activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());
        
        final Member other = createMember("other");
        member.addLink(other);
        // Ensure non activity duplication
        member.addLink(other);
        
        // One activity for member
        assertEquals(1l, Activity.count("from Activity a where a.member = ?", member));
        Activity a = Activity.find("select a from Activity a where a.member = ?", member).first();
        assertActivity(a);
        assertTrue(a instanceof LinkActivity);
        LinkActivity la = (LinkActivity) a;
        assertEquals(member, la.member);
        assertEquals(other, la.linked);
    }
}
