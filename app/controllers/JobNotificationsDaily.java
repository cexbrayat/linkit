package controllers;

import models.NotificationOption;
import play.jobs.On;

/**
 * Asynchronous daily notifications of new activities
 * @author Sryl <cyril.lacote@gmail.com>
 */
//@On("0 0 2 * * ?")  // 2H du matin chaque jour
public class JobNotificationsDaily extends BaseJobNotifications {

    public JobNotificationsDaily() {
        super(NotificationOption.Daily);
    }
}
