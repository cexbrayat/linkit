package controllers;

import play.*;

import java.util.*;
import models.SessionComment;
import models.Member;
import models.Role;
import models.Session;
import models.Talk;
import models.activity.Activity;
import org.apache.commons.lang.StringUtils;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.libs.Images;

public class Sessions extends PageController {

    public static void index() {
        List<Talk> sessions = null;
        if (Security.check(Role.ADMIN_SESSION)) {
            sessions = Talk.findAll();
        } else {
            sessions = Talk.findAllValidated();
        }
        Logger.info(sessions.size() + " sessions");
        render("Sessions/list.html", sessions);
    }

    public static void create(final String speakerLogin) {
        Member speaker = Member.findByLogin(speakerLogin);
        Talk talk = new Talk();
        talk.addSpeaker(speaker);
        render("Sessions/edit.html", talk);
    }

    public static void edit(final Long sessionId) {
        Session talk = Session.findById(sessionId);
        render(talk);
    }

    public static void show(final Long sessionId, String slugify, boolean noCount) {
        Session talk = Session.findById(sessionId);
        // Don't count look when coming from internal redirect
        if (!noCount) {
            talk.lookedBy(Member.findByLogin(Security.connected()));
        }
        List<Activity> activities = Activity.recentsBySession(talk, 1, 5);
        render(talk, activities);
    }

    public static void postComment(
            Long talkId,
            @Required String content) {
        Session talk = Session.findById(talkId);
        if (Validation.hasErrors()) {
            render("Sessions/show.html", talk);
        }

        Member author = Member.findByLogin(Security.connected());
        talk.addComment(new SessionComment(author, talk, content));
        talk.save();
        flash.success("Merci pour votre commentaire %s", author);
        show(talkId, null, true);
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText();
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

    public static void save(@Valid Talk talk, String[] interests, String newInterests) {
        Logger.info("Tentative d'enregistrement de la session " + talk);
        if (interests != null) {
            talk.updateInterests(interests);
        }
        if (newInterests != null) {
            talk.addInterests(StringUtils.splitByWholeSeparator(newInterests, ","));
        }
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            flash.error("Des erreurs sont à corriger dans ta saisie mon ami!");
            render("Sessions/edit.html", talk);
        }
        talk.update();
        flash.success("Session " + talk + " enregistrée");
        Logger.info("Session " + talk + " enregistrée");
        show(talk.id, null, true);
    }
    
    public static void validate(long talkId) {
        Talk talk = Talk.findById(talkId);
        talk.validate();
        show(talkId, null, true);
    }
    
    public static void unvalidate(long talkId) {
        Talk talk = Talk.findById(talkId);
        talk.unvalidate();
        show(talkId, null, true);
    }
}