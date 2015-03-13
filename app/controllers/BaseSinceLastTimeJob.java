package controllers;

import models.GeneralParameter;
import models.Member;
import models.NotificationOption;
import models.Setting;
import models.activity.Activity;
import org.joda.time.DateTime;
import play.Logger;
import play.Play;
import play.jobs.Job;

import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Base job processing new timestamped data since last processing.
 * Persists last processing execution in a {@link GeneralParameter}.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class BaseSinceLastTimeJob extends Job {

    private final String jobId;
    private final String lastNotificationKey;

    public BaseSinceLastTimeJob(String jobId) {
        this.jobId = jobId;
        this.lastNotificationKey = "LastNotification"+jobId;
    }

    @Override
    public void doJob() {
        if (!"test".equals(Play.id)) {
            Logger.debug("BEGIN job notifications %s", jobId);

            // Stores job start time
            final Date now = new Date();
            // Retrieve LastNotification date
            Date lastNotification = getLastNotification();

            doJobBetween(lastNotification, now);

            // Set LastNotification parameter
            setLastNotification(now);
            Logger.debug("END job notifications %s", jobId);
        }
    }

    protected abstract void doJobBetween(Date start, Date end);

    protected Date getLastNotification() {
        String strLastNotification = GeneralParameter.get(this.lastNotificationKey);
        return (strLastNotification == null) ? null : DateTime.parse(strLastNotification).toDate();
    }

    protected void setLastNotification(Date value) {
        GeneralParameter.set(this.lastNotificationKey, new DateTime(value).toString());
    }

}
