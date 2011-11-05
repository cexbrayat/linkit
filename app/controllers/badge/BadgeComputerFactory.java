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
        computers.put(Badge.StaffFriend, new StaffFriendBadgeComputer());
    }
    
    BadgeComputer create(Badge badge) {
        return computers.get(badge);
    }
}
