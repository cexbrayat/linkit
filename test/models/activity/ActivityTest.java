package models.activity;

import models.Member;
import models.Session;
import org.junit.*;

/**
 * Unit tests for {@link Activity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class ActivityTest extends AbstractActivityTest {

    @Test
    public void recents() {
        assertNotNull(Activity.recents(1, 10));
    }

    @Test
    public void recentsByMember() {
        final Member m = Member.all().first();
        assertNotNull(Activity.recentsByMember(m, 1, 10));
    }

    @Test
    public void recentsBySession() {
        final Session s = Session.all().first();
        assertNotNull(Activity.recentsBySession(s, 1, 10));
    }
}
