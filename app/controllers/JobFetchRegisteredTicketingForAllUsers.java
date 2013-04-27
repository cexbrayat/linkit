package controllers;

import helpers.TransactionCallback;
import helpers.TransactionCallbackWithoutResult;
import helpers.TransactionTemplate;
import helpers.ticketing.YurPlan;
import models.Member;
import play.Logger;
import play.db.jpa.NoTransaction;
import play.jobs.Every;
import play.jobs.Job;

import java.util.List;

/**
 * Asynchronous fetch to check if member is registered at the ticketing partner
 * @author Agnes <agnes.crepet@gmail.com>
 */
// AGNES : Disabled YurPlan : not usefull until mixit14!
// FIXME Enable ticketing jobs
@Every("2min")
@NoTransaction
public class JobFetchRegisteredTicketingForAllUsers extends Job {

    private TransactionTemplate txTemplate = new TransactionTemplate();

    @Override
    public void doJob() {
        Logger.info("BEGIN JOB JobFetchRegisteredTicketingUser for all members");

        txTemplate.setReadOnly(true);
        final List<Long> memberIds = txTemplate.execute(new TransactionCallback() {
            public List<Long> doInTransaction() {
                return Member.findAllIds();
            }
        });

        final String token = YurPlan.login();

        txTemplate.setReadOnly(false);
        for (final Long memberId : memberIds) {

            try {
                txTemplate.execute(new TransactionCallbackWithoutResult() {
                    public void doInTransaction() {
                        final Member member = Member.findById(memberId);
                        member.setTicketingRegistered(YurPlan.isRegisteredAttendee(member, token));
                        member.save();
                    }
                });
            } catch (Exception e) {
                Logger.error("Exception while registering user, skipped to next", e);
            }
        }

        Logger.info("END JOB JobFetchRegisteredTicketingUser for all members");
    }
}
