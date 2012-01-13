package controllers;

import models.*;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.data.validation.Valid;
import play.data.validation.Validation;

import java.util.List;
import models.activity.Activity;
import play.data.validation.Required;

public class LightningTalks extends PageController {

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

    public static void show(final Long sessionId, boolean noCount) {
        LightningTalk talk = LightningTalk.findById(sessionId);
        notFoundIfNull(talk);
        // Don't count look when coming from internal redirect
        if (!noCount) {
            talk.lookedBy(Member.findByLogin(Security.connected()));
        }
        List<Activity> activities = Activity.recentsBySession(talk, 1, 5);
        render(talk, activities);
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
        talk.addSpeaker(member);
        talk.save();
        flash.success("LightningTalk " + talk + " enregistré");
        Logger.info("LightningTalk " + talk + " enregistré");
        show(talk.id, true);
    }

    public static void postComment(
            Long talkId,
            @Required String content) {

        LightningTalk talk = LightningTalk.findById(talkId);
        if (Validation.hasErrors()) {
            render("LightningTalks/show.html", talk);
        }

        Member author = Member.findByLogin(Security.connected());
        talk.addComment(new SessionComment(author, talk, content));
        talk.save();
        flash.success("Merci pour votre commentaire %s", author);
        show(talkId, true);
    }

    public static void delete(final Long sessionId) {
        LightningTalk talk = LightningTalk.findById(sessionId);
        talk.delete();
        list();
    }
}