package helpers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;

/**
 * Computer of {@link Badge#Linkator1}, {@link Badge#Linkator5}, {@link Badge#Linkedator1} or {@link Badge#Linkedator5} badges.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class SocialBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        Set<Badge> badges = EnumSet.noneOf(Badge.class);
        final int nbLinks = member.links.size();
        final int nbLinkers = member.linkers.size();
        if (nbLinks >= 1) {
            badges.add(Badge.NewBorn);
        }
        if (nbLinks >= 10) {
            badges.add(Badge.Friendly);
        }
        if (nbLinks >= 50) {
            badges.add(Badge.SocialBeast);
        }
        if (nbLinks >= 100) {
            badges.add(Badge.MadLinker);
        }
        if (nbLinkers >= 1) {
            badges.add(Badge.YouReNotAlone);
        }
        if (nbLinkers >= 10) {
            badges.add(Badge.LocalCelebrity);
        }
        if (nbLinkers >= 50) {
            badges.add(Badge.RockStar);
        }
        if (nbLinkers >= 100) {
            badges.add(Badge.Leader);
        }
        if (nbLinkers >= 200) {
            badges.add(Badge.Idol);
        }
        return badges;
    }
}
