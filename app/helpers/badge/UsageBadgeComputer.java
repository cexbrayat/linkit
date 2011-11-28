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
        final int consecutiveDays = computeActivitiesConsecutiveDays(member, 10);
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
    
    protected int computeActivitiesConsecutiveDays(final Member member, final int maxConsecutives) {
        int consecutiveDays = 0;
        
        final DateTime today = new DateTime();
        int activitiesPage = 1;
        boolean end = false;
        int previousDaysDiff = -1;
        while (!end) {
            // Recent Link-IT activities in desc order
            final List<Activity> activities = Activity.recentsByMember(member, EnumSet.of(ProviderType.LinkIt), activitiesPage++, 20);
            // Ensure activities sorted by at desc
            Collections.sort(activities);

            for (Iterator<Activity> itActivities = activities.iterator(); itActivities.hasNext() && !end;) {
                int daysDiff = Days.daysBetween(new DateTime(itActivities.next().at), today).getDays();
                if (daysDiff-previousDaysDiff==1) {
                    consecutiveDays++;
                } else if (daysDiff - previousDaysDiff > 1) {
                    // A gap in consecutive days : no use to check older activities
                    end = true;
                }
                previousDaysDiff = daysDiff;
                
                if (consecutiveDays >= maxConsecutives) {
                    // We found enough consecutive activities
                    end = true;
                }
            }
            // If no more activities
            if (activities.isEmpty()) end = true;
        }
        return consecutiveDays;
    }
}
