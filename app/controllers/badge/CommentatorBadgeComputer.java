package controllers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Comment;
import models.Member;

/**
 * Computer of {@link Badge#Commentator1} or {@link Badge#Commentator5} badges.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class CommentatorBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        Set<Badge> badges = EnumSet.noneOf(Badge.class);
        // Computing number of comments by Member
        long nbComments = Comment.count("author=?", member);
        if (nbComments >= 1) {
            badges.add(Badge.Commentator1);
        }
        if (nbComments >= 5) {
            badges.add(Badge.Commentator5);
        }
        return badges;
    }
}
