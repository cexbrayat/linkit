package controllers;

import models.*;
import play.Logger;

import play.mvc.Controller;
import play.mvc.With;

@With(SecureLinkIt.class)
public class Votes extends Controller {

    public static long vote(Long talkId, Boolean value) {
        LightningTalk talk = LightningTalk.findById(talkId);
        Member member = Member.findByLogin(Security.connected());
        Logger.info("Vote value: " + value);
        if (member != null && talk != null) {
            Vote vote = Vote.findVote(talk, member);
            if (vote != null) {
                vote.value = !value;
            } else {
                vote = new Vote(talk, member, !value);
            }
            vote.save();
            return Vote.findNumberOfVotesBySession(talk);
        }

        return -1;
    }
}