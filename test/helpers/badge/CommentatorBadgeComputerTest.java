package helpers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Article;
import models.ArticleComment;
import models.Badge;
import models.SessionComment;
import models.Member;
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

    protected void comment(Member m, Session s, final int nbComments) {
        for (int i = 0; i < nbComments; i++) {
            s.addComment(new SessionComment(member, s, "Un commentaire"));
        }
    }

    protected void comment(Member m, Article a, final int nbComments) {
        for (int i = 0; i < nbComments; i++) {
            a.addComment(new ArticleComment(member, a, "Un commentaire"));
        }
    }
    
    @Test
    public void grantedBrave() {
        // Member add one comment
        final Session s = Session.all().first();
        comment(member, s, 1);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Brave), actualBadges);
    }
    
    @Test
    public void grantedTroller() {
        // Member add five comments
        final Session s = Session.all().first();
        final Article a = Article.all().first();
        comment(member, s, 5);  // 5 comments on a session
        comment(member, a, 5);  // 5 comments on an article
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Brave, Badge.Troller), actualBadges);
    }
}
