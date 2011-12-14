package controllers;

import helpers.badge.BadgeComputationContext;
import java.util.concurrent.atomic.AtomicInteger;
import models.activity.Activity;
import play.Logger;
import play.jobs.Job;

/**
 * Asynchronous computations of new badges granted for a given activity.
 * In a dedicated job for dedicated transaction demarcation.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class JobComputeBadgesForActivity extends Job {

    private Long activityId;
    private BadgeComputationContext context;
    
    private static AtomicInteger nbConccurentJobs = new AtomicInteger(0);

    public JobComputeBadgesForActivity(Long activityId, BadgeComputationContext context) {
        this.activityId = activityId;
        this.context = context;
    }

    @Override
    public void doJob() {
        Logger.info("Compute badges for activity %d", activityId);
        Logger.info("Nb of concurrent JobComputeBadgesForActivity at start : %d", nbConccurentJobs.incrementAndGet());

        Activity activity = Activity.findById(activityId);
        activity.computeBadges(context);
                
        Logger.info("Nb of concurrent JobComputeBadgesForActivity at end : %d", nbConccurentJobs.decrementAndGet());
    }
}
