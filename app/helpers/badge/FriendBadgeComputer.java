package helpers.badge;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;
import models.Sponsor;
import models.Staff;

/**
 * Computer of {@link Badge#StaffBestFriend}, {@link Badge#SpeakerFan} and {@link Badge#SponsorFriendly} badges.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class FriendBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        Set<Badge> badges = EnumSet.noneOf(Badge.class);
        
        // Checking staff people linked by member
        Set<Member> staffedLinked = Sets.filter(member.links, new Predicate<Member>() {
            public boolean apply(Member t) {
                return t instanceof Staff;
            }
        });
        if (context.getNbStaff() > 0 && staffedLinked.size() >= context.getNbStaff()) {
            badges.add(Badge.StaffBestFriend);
        }

        // Checking speaker people linked by member
        Set<Member> speakersLinked = Sets.filter(member.links, new Predicate<Member>() {
            public boolean apply(Member m) {
                return m.isSpeaker();
            }
        });
        if (context.getNbSpeakers() > 0 && speakersLinked.size() >= context.getNbSpeakers()) {
            badges.add(Badge.SpeakerFan);
        }

        // Checking sponsor people linked by member
        Set<Member> sponsorsLinked = Sets.filter(member.links, new Predicate<Member>() {
            public boolean apply(Member t) {
                return t instanceof Sponsor;
            }
        });
        if (context.getNbSponsors() > 0 && sponsorsLinked.size() >= context.getNbSponsors()) {
            badges.add(Badge.SponsorFriendly);
        }
        
        return badges;
    }
}
