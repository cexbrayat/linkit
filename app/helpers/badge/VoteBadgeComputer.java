package helpers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;
import models.Vote;

/**
 * Computer of {@link Badge#Supporter} and {@link Badge#Enlightened} badges.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class VoteBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        Set<Badge> badges = EnumSet.noneOf(Badge.class);
        final long nbVotes = Vote.countVotesByMember(member);
        if (nbVotes >= 1) {
            badges.add(Badge.Supporter);
        }
        if (nbVotes >= 10) {
            badges.add(Badge.Enlightened);
        }
        return badges;
    }
}
