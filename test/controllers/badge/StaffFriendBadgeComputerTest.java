package controllers.badge;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import models.Badge;
import models.Staff;
import org.junit.Test;

/**
 * Unit tests for {@link StaffFriendBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class StaffFriendBadgeComputerTest extends AbstractBadgeComputerTest {

    public StaffFriendBadgeComputerTest() {
        super(new StaffFriendBadgeComputer());
    }

    @Test
    public void granted() {
        // Member links all Staff Members
        final List<Staff> staff = Staff.findAll();
        for (Staff s : staff) {
            member.addLink(s);
        }
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.StaffFriend), actualBadges);
    }
}
