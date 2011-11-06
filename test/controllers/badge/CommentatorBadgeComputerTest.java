package controllers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Comment;
import models.Session;
import org.junit.Test;

/**
 * Unit tests for {@link CommentatorBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class CommentatorBadgeComputerTest extends AbstractBadgeComputerTest {

    public CommentatorBadgeComputerTest() {
        super(new CommentatorBadgeComputer());
    }
    
    @Test
    public void grantedCommentator1() {
        // Member add one comment
        final Session s = Session.all().first();
        s.addComment(new Comment(member, s, "Un commentaire"));
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Commentator1), actualBadges);
    }
    
    @Test
    public void grantedCommentator5() {
        // Member add five comments
        final Session s = Session.all().first();
        for (int i = 0; i < 5; i++) {
            s.addComment(new Comment(member, s, "Un commentaire"));
        }
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Commentator1, Badge.Commentator5), actualBadges);
    }
}
