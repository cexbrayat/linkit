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
        BadgeComputer commentator = new CommentatorBadgeComputer();
        BadgeComputer linkator = new LinkBadgeComputer();
        computers.put(Badge.StaffFriend, new StaffFriendBadgeComputer());
        computers.put(Badge.SpeakerFriend, new SpeakerFriendBadgeComputer());
        computers.put(Badge.Commentator1, commentator);
        computers.put(Badge.Commentator5, commentator);
        computers.put(Badge.Linkator1, linkator);
        computers.put(Badge.Linkator5, linkator);
        computers.put(Badge.Linkedator1, linkator);
        computers.put(Badge.Linkedator5, linkator);
    }
    
    /**
     * @param badge Badge to be computed
     * @return {@link BadgeComputer} implementation computing given badge
     */
    public static BadgeComputer getFor(Badge badge) {
        return computers.get(badge);
    }
}
