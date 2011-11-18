package controllers;

import helpers.badge.BadgeComputationContext;
import helpers.badge.BadgeComputer;
import helpers.badge.BadgeComputerFactory;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import models.Badge;
import models.Member;
import models.activity.Activity;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Asynchronous computations of new badges granted to all users. Based on new (uncomputed) {@link Activity}.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Every("20s")
public class JobComputeBadges extends Job {

    @Override
    public void doJob() {
        Logger.info("BEGIN badges computation");

        BadgeComputationContext context = new BadgeComputationContext();

        // Retrieving uncomputed activities
        List<? extends Activity> uncomputedActivities = Activity.uncomputed();
        for (Activity activity : uncomputedActivities) {
            activity.computeBadges(context);
        }
        Logger.info("END badges computation");
    }
}
