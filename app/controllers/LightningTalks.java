package controllers;

import models.*;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.mvc.Controller;

import java.util.List;

public class LightningTalks extends Controller {

    public static void list() {
        List<LightningTalk> sessions = LightningTalk.findAll();
        render(sessions);
    }

    public static void create() {
        LightningTalk talk = new LightningTalk();
        render("LightningTalks/edit.html", talk);
    }

    public static void edit(final Long sessionId) {
        LightningTalk talk = LightningTalk.findById(sessionId);
        render(talk);
    }

    public static void show(final Long sessionId) {
        LightningTalk talk = LightningTalk.findById(sessionId);
        notFoundIfNull(talk);
        render(talk);
    }

    public static void save(@Valid LightningTalk talk, String[] interests, String newInterests) {
        if (interests != null) {
            talk.updateInterests(interests);
        }
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            render("LightningTalks/edit.html", talk);
        }
        if (newInterests != null) {
            talk.addInterests(StringUtils.splitByWholeSeparator(newInterests, ","));
        }
        Member member = Member.findByLogin(Security.connected());
        talk.speaker = member;
        talk.save();
        flash.success("LightningTalk " + talk + " enregistré");
        Logger.info("LightningTalk " + talk + " enregistré");
        show(talk.id);
    }

    public static void delete(final Long sessionId) {
        LightningTalk talk = LightningTalk.findById(sessionId);
        talk.delete();
        list();
    }

    public static long vote(Long talkId, String username, Boolean value) {
        LightningTalk talk = LightningTalk.findById(talkId);
        Member member = Member.findByLogin(username);
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