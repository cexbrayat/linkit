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
        Member bob = Member.findByLogin("bob");
        
        // Non activity for Bob
        assertNull(Activity.find("select a from Activity a where a.member = ?", bob).first());
        
        Member.addLink("bob", "ced");
        
        // One activity for Bob
        Activity a = Activity.find("select a from Activity a where a.member = ?", bob).first();
        assertActivity(a);
        assertTrue(a instanceof LinkActivity);
        LinkActivity la = (LinkActivity) a;
        assertEquals(bob, la.member);
        assertEquals(Member.findByLogin("ced"), la.linked);
    }
}
