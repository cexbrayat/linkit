package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import models.Comment;
import models.Member;
import models.Session;
import models.Speaker;
import net.sf.oval.guard.Post;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.libs.Codec;
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

    public static void show(final Long sessionId) {
        Session talk = Session.findById(sessionId);
        String randomID = Codec.UUID();
        render(talk, randomID);
    }

    public static void postComment(
            Long talkId,
            @Required String login,
            @Required String content,
            @Required String code,
            String randomID) {
        Session talk = Session.findById(talkId);
        if(!Play.id.equals("test")) {
            validation.equals(code, Cache.get(randomID)).message("Es-tu vraiment un humain?");
        }
        if (Validation.hasErrors()) {
            render("Sessions/show.html", talk, randomID);
        }

        Member author = Member.findByLogin(login);
        talk.addComment(new Comment(author, talk, content));
        talk.save();
        flash.success("Merci pour votre commentaire %s", author);
        Cache.delete(randomID);
        show(talkId);
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText();
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

    public static void save(@Valid Session talk) {
        Logger.info("Tentative d'enregistrement de la session " + talk);
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            render("Sessions/edit.html", talk);
        }
        talk.save();
        flash.success("Session " + talk + " enregistrée");
        Logger.info("Session " + talk + " enregistrée");
        show(talk.id);
    }
}