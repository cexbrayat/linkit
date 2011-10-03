package models.activity;

import java.util.List;
import java.util.Map;

import models.Account;
import models.Comment;
import models.GoogleAccount;
import models.Interest;
import models.Member;
import models.Session;
import models.TwitterAccount;
import org.h2.util.StringUtils;
import org.junit.*;
import play.test.*;

/**
 * Unit tests for {@link Activity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class ActivityTest extends UnitTest {

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void addLink() {
        Member bob = Member.findByLogin("bob");
        
        // Non activity for Bob
        assertNull(Activity.find("select a from Activity a where a.member = ?", bob).first());
        
        Member.addLink("bob", "ced");
        
        // One activity for Bob
        Activity a = Activity.find("select a from Activity a where a.member = ?", bob).first();
        assertNotNull(a);
        assertNotNull(a.at);
        assertTrue(a instanceof LinkActivity);
        LinkActivity la = (LinkActivity) a;
        assertEquals(bob, la.member);
        assertEquals(Member.findByLogin("ced"), la.linked);
    }

    @Test
    public void addComment() {
        Session s = Session.find("1 = 1").first();
        
        // Non activity for the session
        assertNull(Activity.find("select a from Activity a where a.session = ?", s).first());
        
        Comment c = new Comment(Member.findByLogin("bob"), s, "Un commentaire");
        s.addComment(c);
        
        // One activity for the session
        Activity a = Activity.find("select a from Activity a where a.session = ?", s).first();
        assertNotNull(a);
        assertNotNull(a.at);
        assertTrue(a instanceof CommentActivity);
        CommentActivity ca = (CommentActivity) a;
        assertEquals(Member.findByLogin("bob"), ca.member);
        assertEquals(s, ca.session);
        assertEquals(c, ca.comment);
    }
}
