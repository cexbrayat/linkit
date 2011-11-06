package controllers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;
import org.junit.Test;

/**
 * Unit tests for {@link LinkBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LinkBadgeComputerTest extends AbstractBadgeComputerTest {

    public LinkBadgeComputerTest() {
        super(new LinkBadgeComputer());
    }
    
    @Test
    public void grantedLinkator1() {
        final Member other = createMember("toto");
        // Member links someone
        member.addLink(other);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Linkator1), actualBadges);
    }
    
    @Test
    public void grantedLinkator5() {
        // Member links 5 other people
        for (int i = 0; i < 5; i++) {
            final Member other = createMember("toto"+i);
            member.addLink(other);
        }
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Linkator1, Badge.Linkator5), actualBadges);
    }
    
    @Test
    public void grantedLinkedator1() {
        // Someone links our member
        final Member other = createMember("toto");
        other.addLink(member);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Linkedator1), actualBadges);
    }
    
    @Test
    public void grantedLinkedator5() {
        // 5 other members link our member
        for (int i = 0; i < 5; i++) {
            final Member other = createMember("toto"+i);
            other.addLink(member);
        }
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Linkedator1, Badge.Linkedator5), actualBadges);
    }
}
