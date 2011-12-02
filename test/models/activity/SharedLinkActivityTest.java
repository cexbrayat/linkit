package models.activity;

import models.SharedLink;
import org.junit.*;

/**
 * Unit tests for {@link SharedLinkActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SharedLinkActivityTest extends AbstractActivityTest {

    @Test
    public void addSharedLink() {
        
        // Non activity for member
        assertNull(Activity.find("select a from Activity a where a.member = ?", member).first());

        final SharedLink link = new SharedLink("test", "http://www.test.fr");
        member.addSharedLink(link);
        member.save();

        // One activity for member
        Activity a = Activity.find("select a from Activity a where a.member = ?", member).first();
        assertActivity(a);
        assertTrue(a instanceof SharedLinkActivity);
        SharedLinkActivity sla = (SharedLinkActivity) a;
        assertSame(member, sla.member);
        assertSame(link, sla.link);
    }
}
