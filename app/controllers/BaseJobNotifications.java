package controllers;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import models.GeneralParameter;
import models.Member;
import models.NotificationOption;
import models.Setting;
import models.activity.Activity;
import org.joda.time.DateTime;
import play.Logger;
import play.Play;
import play.jobs.Job;

/**
 * Base job for mails notifications of last activities
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BaseJobNotifications extends BaseSinceLastTimeJob {

    private NotificationOption option;

    public BaseJobNotifications(NotificationOption option) {
        super(option.toString());
    }

    @Override
    protected void doJobBetween(Date start, Date end) {
        // Find notifiable members
        List<Member> notifiables = Setting.findNotified(option);
        if (!notifiables.isEmpty()) {
            // Find general Activities to be notified to everyone
            List<Activity> general = Activity.notifiablesBetween(start, end);
            for (Member member : notifiables) {
                // Find notifiable activities for Member
                SortedSet<Activity> activities = new TreeSet<Activity>(Activity.notifiablesForBetween(member, start, end));
                // Merge activities for member
                activities.addAll(general);
                if (!activities.isEmpty()) {
                    Logger.info("Notifying user %s of %d activities", member, activities.size());
                    Setting setting = Setting.findByMember(member);
                    Mails.notification(member, setting, activities);
                }
            }
        }
    }
}
