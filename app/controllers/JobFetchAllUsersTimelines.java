package controllers;

import java.util.List;
import models.Member;
import models.activity.StatusActivity;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Asynchronous fetch of user timelines on external providers (Google+, Twitter)
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Every("2min")
public class JobFetchAllUsersTimelines extends Job {

    @Override
    public void doJob() {
        Logger.info("BEGIN fetch timelines");
        List<? extends Member> members = Member.findAll();
        for (Member member : members) {
            StatusActivity.fetchFor(member);
        }
        Logger.info("END fetch timelines");
    }
}
