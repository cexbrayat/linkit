package helpers.badge;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import models.Badge;
import models.Member;
import models.ProviderType;
import models.activity.Activity;
import models.activity.StatusActivity;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Unit tests for {@link UsageBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class UsageBadgeComputerTest extends AbstractBadgeComputerTest {

    public UsageBadgeComputerTest() {
        super(new UsageBadgeComputer());
    }

    /**
     * Create an activity with an age of <code>age</code> hours.
     * @param age Number of hours ago
     * @return new Activity
     */
    static protected Activity createActivity(final Member member, final int age) {
        return new StatusActivity(member, new DateTime().plusHours(-age).toDate(), ProviderType.LinkIt, null, null, null).save();
    }

    /**
     * Create an amount of activities being consecutive on nbConsecutives days
     * @param nbConsecutives number of consecutive days
     * @return new activities
     */
    static protected List<Activity> createConsecutiveActivities(final Member member, final int nbConsecutives) {
        List<Activity> activities = new ArrayList<Activity>(nbConsecutives*3);
        for (int i = 0; i < nbConsecutives; i++) {
            activities.add(createActivity(member, i * 21 + 2));
            activities.add(createActivity(member, i * 23 + 3));
            activities.add(createActivity(member, i * 24 + 1));
        }
        return activities;
    }
    
    @Test
    public void notGrantedBecauseOfGap() {
        List<Activity> activities = createConsecutiveActivities(member, 1);
        activities.add(createActivity(member, 24*3));
        activities.add(createActivity(member, 24*4));
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.noneOf(Badge.class), actualBadges);
    }
    
    @Test
    public void grantedTwoDaysInARow() {
        createConsecutiveActivities(member, 2);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.TwoDaysInARow), actualBadges);
    }
    
    @Test
    public void grantedFiveDaysInARow() {
        createConsecutiveActivities(member, 5);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.TwoDaysInARow, Badge.FiveDaysInARow), actualBadges);
    }
    
    @Test
    public void grantedMixITAddict() {
        createConsecutiveActivities(member, 10);
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.TwoDaysInARow, Badge.FiveDaysInARow, Badge.MixITAddict), actualBadges);
    }
}
