package controllers.badge;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import models.Badge;
import models.Member;
import models.Speaker;
import models.Staff;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link SpeakerFriendBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SpeakerFriendBadgeComputerTest extends UnitTest {

    private SpeakerFriendBadgeComputer computer = new SpeakerFriendBadgeComputer();

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
    public void granted() {
        final Member m = Member.all().first();
        // Member links all speaker Members
        final List<Speaker> speakers = Speaker.findAll();
        for (Speaker s : speakers) {
            m.addLink(s);
        }
        final Set<Badge> actualBadges = computer.compute(m, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.SpeakerFriend), actualBadges);
    }
}
