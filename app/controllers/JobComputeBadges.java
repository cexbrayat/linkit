package controllers;

import helpers.badge.BadgeComputationContext;
import java.util.List;
import models.activity.Activity;
import play.Logger;
import play.Play;
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
        if (!"test".equals(Play.id)) {
            Logger.debug("BEGIN badges computation");

            BadgeComputationContext context = new BadgeComputationContext();

            // Retrieving uncomputed activities
            List<? extends Activity> uncomputedActivities = Activity.uncomputed();
            for (Activity activity : uncomputedActivities) {
                activity.computeBadges(context);
            }
            Logger.debug("END badges computation");
        }   
    }
}
