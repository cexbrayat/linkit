package controllers;

import helpers.TransactionCallback;
import helpers.TransactionCallbackWithoutResult;
import helpers.TransactionTemplate;
import helpers.ticketing.WeezEvent;
import java.util.List;
import java.util.Set;
import models.Member;
import play.Logger;
import play.db.jpa.NoTransaction;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Asynchronous fetch to check if member is registered at the ticketing partner
 * @author Agnes <agnes.crepet@gmail.com>
 */
@Every("3h")
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

        String sessionID = WeezEvent.login();
        WeezEvent.setEvent(sessionID);
        final Set<String> attendees = WeezEvent.getAttendees(sessionID);

        txTemplate.setReadOnly(false);
        for (final Long memberId : memberIds) {

            txTemplate.execute(new TransactionCallbackWithoutResult() {
                public void doInTransaction() {
                    final Member member = Member.findById(memberId);
                    member.setTicketingRegistered(WeezEvent.isRegisteredAttendee(member.email, attendees));
                    member.save();
                }
            });
        }

        Logger.info("END JOB JobFetchRegisteredTicketingUser for all members");
    }
}