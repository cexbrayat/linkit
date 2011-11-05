package controllers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;

/**
 * Computer of {@link Badge#Linkator1}, {@link Badge#Linkator5}, {@link Badge#Linkedator1} or {@link Badge#Linkedator5} badges.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class LinkBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        Set<Badge> badges = EnumSet.noneOf(Badge.class);
        final int nbLinks = member.links.size();
        final int nbLinkers = member.linkers.size();
        if (nbLinks >= 1) {
            badges.add(Badge.Linkator1);
        }
        if (nbLinks >= 5) {
            badges.add(Badge.Linkator5);
        }
        if (nbLinkers >= 1) {
            badges.add(Badge.Linkedator1);
        }
        if (nbLinkers >= 5) {
            badges.add(Badge.Linkedator5);
        }
        return badges;
    }
}
