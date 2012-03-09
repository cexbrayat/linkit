package models.activity;

import models.SessionComment;
import models.Talk;
import org.junit.*;

/**
 * Unit tests for {@link CommentSessionActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class CommentSessionActivityTest extends AbstractActivityTest {

    @Test
    public void addCommentValid() {
        Talk t = createTalk("test");
        // Ensure the talk is valid
        t.valid = true;
        t.save();
        
        // Non activity for the session
        assertEquals(0, Activity.count("session = ?", t));
        
        SessionComment c = new SessionComment(member, t, "Un commentaire");
        t.addComment(c);
        t.save();
        
        // One activity for the session
        assertEquals(1, Activity.count("session = ?", t));
        Activity a = Activity.find("session = ?", t).first();
        assertActivity(a);
        assertTrue(a instanceof CommentSessionActivity);
        CommentSessionActivity ca = (CommentSessionActivity) a;
        assertEquals(member, ca.member);
        assertEquals(t, ca.session);
        assertEquals(c, ca.comment);
    }

    @Test
    public void addCommentNonValid() {
        Talk t = createTalk("test");
        // Ensure the talk is unvalid
        t.valid = false;
        t.save();
        
        // Non activity for the session
        assertEquals(0, Activity.count("session = ?", t));
        
        SessionComment c = new SessionComment(member, t, "Un commentaire");
        t.addComment(c);
        t.save();
        
        // Still no activity for the session
        assertEquals(0, Activity.count("session = ?", t));
        
        t.validate();
        // 1 comment activity amongst others
        assertEquals(1, CommentSessionActivity.count("session = ?", t));
        Activity a = CommentSessionActivity.find("session = ?", t).first();
        assertActivity(a);
        assertTrue(a instanceof CommentSessionActivity);
        CommentSessionActivity ca = (CommentSessionActivity) a;
        assertEquals(member, ca.member);
        assertEquals(t, ca.session);
        assertEquals(c, ca.comment);
    }
}
