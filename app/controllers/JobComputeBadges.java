package controllers;

import helpers.TransactionCallback;
import helpers.TransactionCallbackWithoutResult;
import helpers.TransactionTemplate;
import helpers.badge.BadgeComputationContext;
import java.util.List;
import models.activity.Activity;
import play.Play;
import play.db.jpa.NoTransaction;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Asynchronous computations of new badges granted to all users. Based on new (uncomputed) {@link Activity}s.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Every("5min")
@NoTransaction
public class JobComputeBadges extends Job {

    private TransactionTemplate txTemplate = new TransactionTemplate(true);

    @Override
    public void doJob() {
        if (!"test".equals(Play.id)) {

            final BadgeComputationContext context = new BadgeComputationContext();

            // Retrieving uncomputedIds activities
            List<Long> uncomputedActivityIds = txTemplate.execute(new TransactionCallback() {
                public List<Long> doInTransaction() {
                    return Activity.uncomputedIds();
                }
            });
            
            // Not read-only transactions
            txTemplate.setReadOnly(false);
            for (final Long activityId : uncomputedActivityIds) {
                
                txTemplate.execute(new TransactionCallbackWithoutResult() {
                    public void doInTransaction() {
                        Activity activity = Activity.findById(activityId);
                        activity.computeBadges(context);
                    }
                });
            }
        }   
    }
}
