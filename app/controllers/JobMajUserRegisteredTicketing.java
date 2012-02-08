package controllers;

import helpers.ticketing.WeezEvent;
import models.Member;
import play.Logger;
import play.jobs.Job;

/**
 * Asynchronous fetch to check if users are registered at the ticketing partner
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class JobMajUserRegisteredTicketing extends Job {

    private long idMember;

    public JobMajUserRegisteredTicketing(long idMember) {
        this.idMember = idMember;
    }

    @Override
    public void doJob() {
        Logger.info("BEGIN JOB JobMajUserRegisteredTicketing for member with id %d", idMember);
        Member member = Member.findById(idMember);
        if (member != null) {
            WeezEvent.updateRegisteredAttendee(member);
        } else {
            Logger.error("JOB JobMajUserRegisteredTicketing, member id %d not found", idMember);
        }
        Logger.info("END JOB JobMajUserRegisteredTicketing for member %d", idMember);

    }
}
