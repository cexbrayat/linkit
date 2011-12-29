package controllers;

import com.google.gson.JsonArray;
import helpers.TransactionCallbackWithoutResult;
import helpers.TransactionTemplate;
import helpers.ticketing.WeezEvent;
import java.util.List;
import models.Member;
import play.Logger;
import play.db.jpa.NoTransaction;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Asynchronous fetch to check if member is registered at the ticketing partner
 * @author Agnes <agnes.crepet@gmail.com>
 */
@Every("10800s")
@NoTransaction
public class JobFetchRegisteredTicketingForAllUsers extends Job {

    private TransactionTemplate txTemplate = new TransactionTemplate();

    @Override
    public void doJob() {
        Logger.info("BEGIN JOB JobFetchRegisteredTicketingUser for all members");
        txTemplate.execute(new TransactionCallbackWithoutResult() {

            public void doInTransaction() {
                List<Member> members = Member.findAll();
                String sessionID = WeezEvent.login();
                WeezEvent.setEvent(sessionID);
                final List<String> allAttendees = WeezEvent.getAttendees(sessionID);

                for (final Member member : members) {
                    if (WeezEvent.isRegisteredAttendee(member.email, allAttendees)) {
                        member.ticketingRegistered = true;
                        member.save();
                    }
                }
            }
        });

        Logger.info("END JOB JobFetchRegisteredTicketingUser for all members");

    }
}
