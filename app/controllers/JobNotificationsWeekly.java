package controllers;

import models.NotificationOption;
import play.jobs.On;

/**
 * Asynchronous daily notifications of new activities
 * @author Sryl <cyril.lacote@gmail.com>
 */
@On("0 0 1 ? * MON")  // 1H du matin tous les lundi matin
public class JobNotificationsWeekly extends BaseJobNotifications {

    public JobNotificationsWeekly() {
        super(NotificationOption.Weekly);
    }
}
