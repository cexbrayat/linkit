package controllers;

import models.Member;
import models.activity.StatusActivity;
import play.Logger;
import play.jobs.Job;

/**
 * Asynchronous fetch of one user timelines on external providers (Google+, Twitter)
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class JobFetchUserTimeline extends Job {

    private Member member;

    public JobFetchUserTimeline(Member member) {
        this.member = member;
    }
    
    @Override
    public void doJob() {
        Logger.info("BEGIN JOB fetch timelines for " + member);
        StatusActivity.fetchForMember(member.id);
        Logger.info("END JOB fetch timelines for " + member);
    }
}
