package controllers.badge;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import models.Badge;
import models.Speaker;
import org.junit.Test;

/**
 * Unit tests for {@link SpeakerFriendBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SpeakerFriendBadgeComputerTest extends AbstractBadgeComputerTest {

    public SpeakerFriendBadgeComputerTest() {
        super(new SpeakerFriendBadgeComputer());
    }
    
    @Test
    public void granted() {
        // Member links all speaker Members
        final List<Speaker> speakers = Speaker.findAll();
        for (Speaker s : speakers) {
            member.addLink(s);
        }
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.SpeakerFriend), actualBadges);
    }
}
