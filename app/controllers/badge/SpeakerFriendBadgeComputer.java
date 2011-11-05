package controllers.badge;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;
import models.Speaker;

/**
 * Computer of {@link Badge#SpeakerFriend} badge.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class SpeakerFriendBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        // Checking speaker people linked by member
        Set<Member> speakersLinked = Sets.filter(member.links, new Predicate<Member>() {
            public boolean apply(Member t) {
                return t instanceof Speaker;
            }
        });
        if (speakersLinked.size() >= context.getNbSpeakers()) {
            return EnumSet.of(Badge.SpeakerFriend);
        } else {
            return EnumSet.noneOf(Badge.class);
        }
    }
}
