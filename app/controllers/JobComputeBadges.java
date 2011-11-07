package controllers;

import controllers.badge.BadgeComputationContext;
import controllers.badge.BadgeComputer;
import controllers.badge.BadgeComputerFactory;
import java.util.EnumSet;
import java.util.HashSet;
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
            // Potential badges triggered by this activity
            Set<Badge> potentialBadges = activity.getPotentialTriggeredBadges();

            // FIXME CLA compute badge for linked member!
            computeForMember(activity.member, potentialBadges, context);

            // Flagging current activity as computed (whatever if we earned badges or not)
            activity.badgeComputationDone = true;
            activity.save();
        }
        Logger.info("END badges computation");
    }

    protected void computeForMember(Member member, Set<Badge> potentialBadges, BadgeComputationContext context) {

        // Member potentially null
        if (member != null) {

            // Avoid to recompute an already earned badge
            potentialBadges.removeAll(member.badges);
            
            // Retrieving badge computers for thoses potential badges
            Set<BadgeComputer> computers = new HashSet<BadgeComputer>();
            for (Badge badge : potentialBadges) {
                computers.add(BadgeComputerFactory.getFor(badge));
            }

            // Computing all granted badges
            Set<Badge> grantedBadges = EnumSet.noneOf(Badge.class);
            for (BadgeComputer computer : computers) {
                grantedBadges.addAll(computer.compute(member, context));
            }

            // Granting earned badges to member
            if (!grantedBadges.isEmpty()) {
                for (Badge badge : grantedBadges) {
                    Logger.debug("Le membre %s se voit attribuer le badge %s", member, badge);
                    member.addBadge(badge);
                }
                member.save();
            }
        }
    }
}
