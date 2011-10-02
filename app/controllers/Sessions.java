package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import models.Session;
import models.Speaker;
import models.Track;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;

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
        render(talk);
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