package controllers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Comment;
import models.Member;
import models.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link CommentatorBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class CommentatorBadgeComputerTest extends UnitTest {

    private CommentatorBadgeComputer computer = new CommentatorBadgeComputer();
    
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
    public void notGranted() {
        final Member m = Member.all().first();
        final Set<Badge> actualBadges = computer.compute(m, new BadgeComputationContext());
        assertTrue(actualBadges.isEmpty());
    }
    
    @Test
    public void grantedCommentator1() {
        final Member m = Member.all().first();
        // Member add one comment
        final Session s = Session.all().first();
        s.addComment(new Comment(m, s, "Un commentaire"));
        final Set<Badge> actualBadges = computer.compute(m, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Commentator1), actualBadges);
    }
    
    @Test
    public void grantedCommentator5() {
        final Member m = Member.all().first();
        // Member add five comments
        final Session s = Session.all().first();
        for (int i = 0; i < 5; i++) {
            s.addComment(new Comment(m, s, "Un commentaire"));
        }
        final Set<Badge> actualBadges = computer.compute(m, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Commentator1, Badge.Commentator5), actualBadges);
    }
}
