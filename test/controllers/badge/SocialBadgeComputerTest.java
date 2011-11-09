package controllers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;
import org.junit.Test;

/**
 * Unit tests for {@link SocialBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class SocialBadgeComputerTest extends AbstractBadgeComputerTest {

    public SocialBadgeComputerTest() {
        super(new SocialBadgeComputer());
    }

    protected void addLinks(Member m, final int nbLinks) {
        for (int i = 0; i < nbLinks; i++) {
            final Member other = createMember("toto"+i);
            m.addLink(other);
        }
    }

    protected void addLinkers(Member m, final int nbLinkers) {
        for (int i = 0; i < nbLinkers; i++) {
            final Member other = createMember("toto"+i);
            other.addLink(m);
        }
    }
    
    @Test
    public void grantedNewBorn() {
        addLinks(member, 1);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.NewBorn), actualBadges);
    }
    
    @Test
    public void grantedFriendly() {
        addLinks(member, 10);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.NewBorn, Badge.Friendly), actualBadges);
    }
    
    @Test
    public void grantedSocialBeast() {
        addLinks(member, 50);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.NewBorn, Badge.Friendly, Badge.SocialBeast), actualBadges);
    }
    
    @Test
    public void grantedMadLinker() {
        addLinks(member, 100);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.NewBorn, Badge.Friendly, Badge.SocialBeast, Badge.MadLinker), actualBadges);
    }
    
    @Test
    public void grantedYouReNotAlone() {
        // Someone links our member
        addLinkers(member, 1);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.YouReNotAlone), actualBadges);
    }
    
    @Test
    public void grantedLocalCelebrity() {
        // 10 other members link our member
        addLinkers(member, 10);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.YouReNotAlone, Badge.LocalCelebrity), actualBadges);
    }
    
    @Test
    public void grantedRockStar() {
        // 50 other members link our member
        addLinkers(member, 50);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.YouReNotAlone, Badge.LocalCelebrity, Badge.RockStar), actualBadges);
    }
    
    @Test
    public void grantedLeader() {
        // 100 other members link our member
        addLinkers(member, 100);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.YouReNotAlone, Badge.LocalCelebrity, Badge.RockStar, Badge.Leader), actualBadges);
    }
    
    @Test
    public void grantedIdol() {
        // 200 other members link our member
        addLinkers(member, 200);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.YouReNotAlone, Badge.LocalCelebrity, Badge.RockStar, Badge.Leader, Badge.Idol), actualBadges);
    }
}
