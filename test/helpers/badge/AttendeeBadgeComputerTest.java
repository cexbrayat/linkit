package helpers.badge;

import java.util.EnumSet;
import models.Badge;
import org.junit.Test;

/**
 * Unit tests for {@link AttendeeBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class AttendeeBadgeComputerTest extends AbstractBadgeComputerTest {

    public AttendeeBadgeComputerTest() {
        super(new AttendeeBadgeComputer());
    }
    
    @Test
    public void grantedAttendee() {
        member.setTicketingRegistered(true);
        member.save();
        assertEquals(EnumSet.of(Badge.Attendee), computer.compute(member, new BadgeComputationContext()));
    }
}
