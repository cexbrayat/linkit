package controllers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Comment;
import models.Member;

/**
 * Computer of {@link Badge#Brave} or {@link Badge#Troller} badges.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class CommentatorBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        Set<Badge> badges = EnumSet.noneOf(Badge.class);
        // Computing number of comments by Member
        long nbComments = Comment.countByMember(member);
        if (nbComments >= 1) {
            badges.add(Badge.Brave);
        }
        if (nbComments >= 10) {
            badges.add(Badge.Troller);
        }
        return badges;
    }
}
