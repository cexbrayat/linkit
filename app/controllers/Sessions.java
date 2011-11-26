package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import models.SessionComment;
import models.Member;
import models.Session;
import models.Speaker;
import models.activity.Activity;
import org.apache.commons.lang.StringUtils;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.libs.Images;

public class Sessions extends Controller {

    public static void index() {
        List<Session> sessions = Session.findAll();
        Logger.info(sessions.size() + " sessions");
        render("Sessions/list.html", sessions);
    }

    public static void create(final String speakerLogin) {
        Speaker speaker = Speaker.findByLogin(speakerLogin);
        Session talk = new Session();
        talk.addSpeaker(speaker);
        render("Sessions/edit.html", talk);
    }

    public static void edit(final Long sessionId) {
        Session talk = Session.findById(sessionId);
        render("Sessions/edit.html", talk);
    }

    public static void show(final Long sessionId, boolean noCount) {
        Session talk = Session.findById(sessionId);
        // Don't count look when coming from internal redirect
        if (!noCount) {
            talk.lookedBy(Member.findByLogin(Security.connected()));
        }
        List<Activity> activities = Activity.recentsBySession(talk, 1, 10);
        render(talk, activities);
    }
    
    public static void postComment(
            Long talkId,
            @Required String login,
            @Required String content) {
        Session talk = Session.findById(talkId);
        if (Validation.hasErrors()) {
            render("Sessions/show.html", talk);
        }

        Member author = Member.findByLogin(login);
        talk.addComment(new SessionComment(author, talk, content));
        talk.save();
        flash.success("Merci pour votre commentaire %s", author);
        show(talkId, true);
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText();
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

    public static void save(@Valid Session talk,String[] interests, String newInterests) {
        Logger.info("Tentative d'enregistrement de la session " + talk);
        if (interests != null) {
            talk.updateInterests(interests);
        }
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            render("Sessions/edit.html", talk);
        }
        if (newInterests != null) {
            talk.addInterests(StringUtils.splitByWholeSeparator(newInterests, ","));
        }
        talk.update();
        flash.success("Session " + talk + " enregistrée");
        Logger.info("Session " + talk + " enregistrée");
        show(talk.id, true);
    }
}