package helpers.badge;

import java.util.EnumSet;
import models.Badge;
import models.LightningTalk;
import models.Member;
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

    private LightningTalk createLT(Member... speakers) {
        LightningTalk lt = new LightningTalk();
        for (Member s : speakers) {
            lt.addSpeaker(s);
        }
        return lt.save();
    }

    @Test
    public void grantedSpeaker() {
        // Member become speaker of a validated talk
        final Talk t = createTalk(member);
        assertEquals(EnumSet.noneOf(Badge.class), computer.compute(member, new BadgeComputationContext()));
        t.validate();
        assertEquals(EnumSet.of(Badge.Speaker), computer.compute(member, new BadgeComputationContext()));
    }

    @Test
    public void grantedSpeakerPadawan() {
        // Member become speaker of a LT
        final LightningTalk lt = createLT(member);
        assertEquals(EnumSet.of(Badge.SpeakerPadawan), computer.compute(member, new BadgeComputationContext()));
    }
}
