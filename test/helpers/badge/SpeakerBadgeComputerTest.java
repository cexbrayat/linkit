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

    private Talk createTalk(Member... speakers) {
        Talk t = new Talk();
        for (Member s : speakers) {
            t.addSpeaker(s);
        }
        return t.save();
    }

    @Test
    public void grantedSpeaker() {
        // Member become speaker of a validated talk
        final Talk t = createTalk(member);
        assertEquals(EnumSet.noneOf(Badge.class), computer.compute(member, new BadgeComputationContext()));
        t.validate();
        assertEquals(EnumSet.of(Badge.Speaker), computer.compute(member, new BadgeComputationContext()));
    }
}
