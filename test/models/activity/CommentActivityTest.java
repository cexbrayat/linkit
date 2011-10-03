package models.activity;

import models.Comment;
import models.Member;
import models.Session;
import org.junit.*;

/**
 * Unit tests for {@link CommentActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class CommentActivityTest extends AbstractActivityTest {

    @Test
    public void addComment() {
        Session s = Session.all().first();
        
        // Non activity for the session
        assertNull(Activity.find("select a from Activity a where a.session = ?", s).first());
        
        Comment c = new Comment(Member.findByLogin("bob"), s, "Un commentaire");
        s.addComment(c);
        
        // One activity for the session
        Activity a = Activity.find("select a from Activity a where a.session = ?", s).first();
        assertActivity(a);
        assertTrue(a instanceof CommentActivity);
        CommentActivity ca = (CommentActivity) a;
        assertEquals(Member.findByLogin("bob"), ca.member);
        assertEquals(s, ca.session);
        assertEquals(c, ca.comment);
    }
}
