package controllers;

import java.util.List;
import models.Member;
import models.activity.StatusActivity;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.db.jpa.NoTransaction;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Asynchronous fetch of user timelines on external providers (Google+, Twitter)
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Every("5min")
public class JobFetchAllUsersTimelines extends Job {

    @Override
    @NoTransaction
    public void doJob() {
        Logger.info("BEGIN fetch timelines");
        List<? extends Member> members = Member.findAll();
        for (Member member : members) {
            
            JPAPlugin.startTx(false);
            
            StatusActivity.fetchForMember(member.id);
            
            JPA.em().getTransaction().commit();
            JPAPlugin.closeTx(false);
        }
        Logger.info("END fetch timelines");
    }
}
