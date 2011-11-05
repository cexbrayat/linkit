package controllers.badge;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;
import models.Staff;

/**
 * Computer of {@link Badge#StaffFriend} badge.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class StaffFriendBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        // Checking staff people linked by member
        Set<Member> staffedLinked = Sets.filter(member.links, new Predicate<Member>() {
            public boolean apply(Member t) {
                return t instanceof Staff;
            }
        });
        if (staffedLinked.size() >= context.getNbStaff()) {
            return EnumSet.of(Badge.StaffFriend);
        } else {
            return EnumSet.noneOf(Badge.class);
        }
    }
}
