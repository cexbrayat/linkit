package controllers;

import models.NotificationOption;
import play.jobs.Every;

/**
 * Asynchronous "hourly" notifications of new activities
 * @author Sryl <cyril.lacote@gmail.com>
 */
//@Every("1h")
public class JobNotificationsHourly extends BaseJobNotifications {

    public JobNotificationsHourly() {
        super(NotificationOption.Hourly);
    }
}
