package controllers;

import models.ConferenceEvent;
import models.Member;
import models.Session;
import models.Vote;
import play.mvc.Controller;
import play.mvc.With;

@With(SecureLinkIt.class)
public class Votes extends Controller {

    public static long vote(Long talkId, Boolean value) {
        Session talk = Session.findById(talkId);
        Member member = Member.findByLogin(Security.connected());
        if (member != null && talk != null) {
            // Can't vote on a passed event
            if (talk.event.isCurrent()) {
                Vote vote = Vote.findVote(talk, member);
                if (vote != null) {
                    vote.value = !value;
                } else {
                    vote = new Vote(talk, member, !value);
                }
                vote.save();
                return Vote.findNumberOfVotesBySession(talk);
            }
        }
        return -1;
    }
}