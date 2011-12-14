package controllers;

import helpers.TransactionCallback;
import helpers.TransactionCallbackWithoutResult;
import helpers.TransactionTemplate;
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

            final BadgeComputationContext context = new BadgeComputationContext();

            // Retrieving uncomputedIds activities
            List<Long> uncomputedActivityIds = Activity.uncomputedIds();            
            for (final Long activityId : uncomputedActivityIds) {
                // Start a dedicated job for this activity to have a dedicated transaction
                new JobComputeBadgesForActivity(activityId, context).now();
            }
        }   
    }
}
