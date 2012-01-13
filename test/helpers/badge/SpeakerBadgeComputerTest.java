package helpers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Article;
import models.ArticleComment;
import models.Badge;
import models.SessionComment;
import models.Member;
import models.Session;
import models.Talk;
import org.junit.Test;

/**
 * Unit tests for {@link SpeakerBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SpeakerBadgeComputerTest extends AbstractBadgeComputerTest {

    public SpeakerBadgeComputerTest() {
        super(new SpeakerBadgeComputer());
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
    public void grantedSpeaker() {
        // Member become speaker of a validated talk
        final Talk t = Talk.all().first();
        t.addSpeaker(member);
        t.validate();
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Speaker), actualBadges);
    }
}
