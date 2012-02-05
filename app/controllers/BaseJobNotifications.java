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
import play.jobs.Job;

/**
 * Base job for mails notifications of last activities
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BaseJobNotifications extends Job {

    private NotificationOption option;
    private static final String PARAM_LAST_NOTIFICATION_PREFIX = "LastNotification";

    public BaseJobNotifications(NotificationOption option) {
        this.option = option;
    }

    @Override
    public void doJob() {
        if (!"test".equals(Play.id)) {
            Logger.info("BEGIN job notifications %s", option);

            // Stores job start time
            final Date begin = new Date();
            // Retrieve LastNotification date
            Date lastNotification = getLastNotification();
            // Find general Activities to be notified to everyone
            List<Activity> general = Activity.notifiablesBetween(lastNotification, begin);
            // Find notifiable members
            List<Member> notifiables = Member.findNotified(option);
            for (Member member : notifiables) {
                // Find notifiable activities for Member
                List<Activity> activities = Activity.notifiablesForBetween(member, lastNotification, begin);
                // Merge activities for member
                activities.addAll(general);
                Collections.sort(activities);
                if (!activities.isEmpty()) {
                    Logger.info("Notifying user %s of %d activities", member, activities.size());
                    Mails.notification(member, activities);
                }
            }
            // Set LastNotification parameter
            setLastNotification(begin);
            Logger.info("END job notifications %s", option);
        }
    }
    
    private String getParamLastNotificationKey() {
        return PARAM_LAST_NOTIFICATION_PREFIX+option.name();
    }
    
    protected Date getLastNotification() {
        String strLastNotification = GeneralParameter.get(getParamLastNotificationKey());
        return (strLastNotification == null) ? null : DateTime.parse(strLastNotification).toDate();
    }

    protected void setLastNotification(Date value) {
        GeneralParameter.set(getParamLastNotificationKey(), new DateTime(value).toString());
    }

}
