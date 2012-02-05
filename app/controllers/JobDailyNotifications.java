package controllers;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import models.GeneralParameter;
import models.Member;
import models.NotificationOption;
import models.activity.Activity;
import org.joda.time.DateTime;
import play.Logger;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Asynchronous computations of new badges granted to all users. Based on new (uncomputed) {@link Activity}s.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Every("1min")
public class JobDailyNotifications extends Job {

    private static final String LAST_NOTIFICATION_PARAM_KEY = "LastNotification";
    private static final NotificationOption MEMBERS_OPTION = NotificationOption.Daily;
    @Override
    public void doJob() {
        if (!"test".equals(Play.id)) {
            Logger.info("BEGIN job daily notification");
            
            // Stores job start time
            final Date begin = new Date();

            // Retrieve LastNotification date
            Date lastNotification = getLastNotification();
            
            // Find general Activities to be notified to everyone
            List<Activity> general = Activity.notifiablesBetween(lastNotification, begin);

            // Find notifiable members
            List<Member> notifiables = Member.findNotified(MEMBERS_OPTION);
            for (Member member : notifiables) {
                // Find notifiable activities for Member
                List<Activity> activities = Activity.notifiablesForBetween(member, lastNotification, begin);
                // Merge activities for member
                activities.addAll(general);
                Collections.sort(activities);
                
                if (!activities.isEmpty()) {
                    Logger.info("Notifiing user %s of %d activities", member, activities.size());
                    Mails.notification(member, activities);
                }
            }
            
            // Set LastNotification parameter
            setLastNotification(begin);

            Logger.info("END job daily notification");
        }   
    }

    private Date getLastNotification() {
        String strLastNotification = GeneralParameter.get(LAST_NOTIFICATION_PARAM_KEY);
        return (strLastNotification == null) ? null : DateTime.parse(strLastNotification).toDate();
    }

    private void setLastNotification(Date value) {
        GeneralParameter.set(LAST_NOTIFICATION_PARAM_KEY, new DateTime(value).toString());
    }
}
