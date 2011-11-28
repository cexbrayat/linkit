package models.activity;

import models.SessionComment;
import models.Member;
import models.Session;
import org.junit.*;

/**
 * Unit tests for {@link CommentSessionActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class CommentSessionActivityTest extends AbstractActivityTest {

    @Test
    public void addComment() {
        Session s = Session.all().first();
        
        // Non activity for the session
        assertEquals(0, Activity.count("session = ?", s));
        
        SessionComment c = new SessionComment(member, s, "Un commentaire");
        s.addComment(c);
        s.save();
        
        // One activity for the session
        assertEquals(1, Activity.count("session = ?", s));
        Activity a = Activity.find("session = ?", s).first();
        assertActivity(a);
        assertTrue(a instanceof CommentSessionActivity);
        CommentSessionActivity ca = (CommentSessionActivity) a;
        assertEquals(member, ca.member);
        assertEquals(s, ca.session);
        assertEquals(c, ca.comment);
    }
}
