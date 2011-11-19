package helpers.badge;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import models.Badge;
import models.Member;
import models.ProviderType;
import models.activity.Activity;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Computer of {@link Badge#TwoDaysInARow}, {@link Badge#FiveDaysInARow} and {@link Badge#MixITAddict} badges.
 * @author Sryl <cyril.lacote@gmail.com>
 */
class UsageBadgeComputer implements BadgeComputer {

    public Set<Badge> compute(final Member member, BadgeComputationContext context) {
        Set<Badge> badges = EnumSet.noneOf(Badge.class);
        // Recent Link-IT activities in desc order
        final List<Activity> activities = Activity.recentsByMember(member, EnumSet.of(ProviderType.LinkIt), 1, 100);
        final int consecutiveDays = computeConsecutiveDays(activities, 10);
        if (consecutiveDays >= 2) {
            badges.add(Badge.TwoDaysInARow);
        }
        if (consecutiveDays >= 5) {
            badges.add(Badge.FiveDaysInARow);
        }
        if (consecutiveDays >= 10) {
            badges.add(Badge.MixITAddict);
        }
        return badges;
    }
    
    protected int computeConsecutiveDays(List<Activity> activities, final int maxConsecutives) {
        int consecutiveDays = 0;
        
        // Ensure activities sorted by at desc
        Collections.sort(activities);
        
        int previousDaysDiff = -1;
        boolean gap = false;
        final DateTime today = new DateTime();
        // Pour chaque activité survenue il y a moins de 10 jours
        for (Iterator<Activity> itActivities = activities.iterator(); itActivities.hasNext() && !gap && consecutiveDays < maxConsecutives;) {
            int daysDiff = Days.daysBetween(new DateTime(itActivities.next().at), today).getDays();
            if (daysDiff-previousDaysDiff==1) {
                consecutiveDays++;
            } else if (daysDiff - previousDaysDiff > 1) {
                gap = true;
            }
            previousDaysDiff = daysDiff;
        }
        return consecutiveDays;
    }
}
