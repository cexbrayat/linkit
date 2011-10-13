package controllers;

import models.Member;
import models.activity.StatusActivity;
import play.Logger;
import play.jobs.Job;

/**
 * Asynchronous fetch of on user timelines on external providers (Google+, Twitter)
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
        // Reload member
        member = Member.findById(member.id);
        StatusActivity.fetchFor(member);
        Logger.info("END JOB fetch timelines for " + member);
    }
}
