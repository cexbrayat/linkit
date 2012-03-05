package helpers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;

/**
 * Computer of {@link Badge#Attendee} badges.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class AttendeeBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        Set<Badge> badges = EnumSet.noneOf(Badge.class);
        if (member.ticketingRegistered) {
            badges.add(Badge.Attendee);
        }
        return badges;
    }
}
