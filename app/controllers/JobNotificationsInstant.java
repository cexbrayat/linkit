package controllers;

import models.NotificationOption;
import play.jobs.Every;

/**
 * Asynchronous "instant" notifications of new activities (actually, every 5 minutes...)
 * @author Sryl <cyril.lacote@gmail.com>
 */
//@Every("5min")
public class JobNotificationsInstant extends BaseJobNotifications {

    public JobNotificationsInstant() {
        super(NotificationOption.Instant);
    }
}
