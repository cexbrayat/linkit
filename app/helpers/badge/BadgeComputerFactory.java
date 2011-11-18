package helpers.badge;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import models.Badge;

/**
 * Factory for {@link BadgeComputer} implementations, for a given {@link Badge}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeComputerFactory {

    private static final Map<Badge, BadgeComputer> COMPUTERS = new EnumMap<Badge, BadgeComputer>(Badge.class);
    static {
        BadgeComputer friend = new FriendBadgeComputer();
        BadgeComputer commentator = new CommentatorBadgeComputer();
        BadgeComputer social = new SocialBadgeComputer();
        COMPUTERS.put(Badge.StaffBestFriend, friend);
        COMPUTERS.put(Badge.SpeakerFan, friend);
        COMPUTERS.put(Badge.SponsorFriendly, friend);
        COMPUTERS.put(Badge.Brave, commentator);
        COMPUTERS.put(Badge.Troller, commentator);
        COMPUTERS.put(Badge.NewBorn, social);
        COMPUTERS.put(Badge.Friendly, social);
        COMPUTERS.put(Badge.SocialBeast, social);
        COMPUTERS.put(Badge.MadLinker, social);
        COMPUTERS.put(Badge.YouReNotAlone, social);
        COMPUTERS.put(Badge.LocalCelebrity, social);
        COMPUTERS.put(Badge.RockStar, social);
        COMPUTERS.put(Badge.Leader, social);
        COMPUTERS.put(Badge.Idol, social);
    }
    
    /**
     * @param badge Badge to be computed
     * @return {@link BadgeComputer} implementation computing given badge
     */
    public static BadgeComputer getFor(Badge badge) {
        return COMPUTERS.get(badge);
    }
    
    /**
     * @param badges Badges to be computed
     * @return Set of {@link BadgeComputer} implementations computing given badges
     */
    public static Set<BadgeComputer> getFor(Collection<Badge> badges) {
        Set<BadgeComputer> computers = new HashSet<BadgeComputer>();
        for (Badge badge : badges) {
            computers.add(BadgeComputerFactory.getFor(badge));
        }
        return computers;
    }
}
