package controllers.badge;

import java.util.EnumMap;
import java.util.Map;
import models.Badge;

/**
 * Factory for {@link BadgeComputer} implementations, for a given {@link Badge}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeComputerFactory {

    private static final Map<Badge, BadgeComputer> computers = new EnumMap<Badge, BadgeComputer>(Badge.class);
    static {
        BadgeComputer friend = new FriendBadgeComputer();
        BadgeComputer commentator = new CommentatorBadgeComputer();
        BadgeComputer social = new SocialBadgeComputer();
        computers.put(Badge.StaffBestFriend, friend);
        computers.put(Badge.SpeakerFan, friend);
        computers.put(Badge.SponsorFriendly, friend);
        computers.put(Badge.Brave, commentator);
        computers.put(Badge.Troller, commentator);
        computers.put(Badge.NewBorn, social);
        computers.put(Badge.Friendly, social);
        computers.put(Badge.SocialBeast, social);
        computers.put(Badge.MadLinker, social);
        computers.put(Badge.YouReNotAlone, social);
        computers.put(Badge.LocalCelebrity, social);
        computers.put(Badge.RockStar, social);
        computers.put(Badge.Leader, social);
        computers.put(Badge.Idol, social);
    }
    
    /**
     * @param badge Badge to be computed
     * @return {@link BadgeComputer} implementation computing given badge
     */
    public static BadgeComputer getFor(Badge badge) {
        return computers.get(badge);
    }
}
