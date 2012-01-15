package helpers.badge;

import java.util.EnumSet;
import models.Badge;
import models.LightningTalk;
import models.Member;
import models.Vote;
import org.junit.Test;

/**
 * Unit tests for {@link SpeakerBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class VoteBadgeComputerTest extends AbstractBadgeComputerTest {

    public VoteBadgeComputerTest() {
        super(new VoteBadgeComputer());
    }

    private LightningTalk createLT() {
        return new LightningTalk().save();
    }

    private void vote(LightningTalk lt, Member m, int nb) {
        for (int i = 0; i < nb; i++) {
            new Vote(lt, member, true).save();
        }
    }
    
    @Test
    public void grantedSupporter() {
        final LightningTalk lt = createLT();
        vote(lt, member, 1);
        assertEquals(EnumSet.of(Badge.Supporter), computer.compute(member, new BadgeComputationContext()));
    }
    
    @Test
    public void grantedEnlightened() {
        final LightningTalk lt = createLT();
        vote(lt, member, 10);
        assertEquals(EnumSet.of(Badge.Supporter, Badge.Enlightened), computer.compute(member, new BadgeComputationContext()));
    }
}
