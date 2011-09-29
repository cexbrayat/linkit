package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import models.Session;
import models.Speaker;
import models.Track;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import play.data.validation.Required;

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

    public static void save(Long id, @Required String title, @Required String summary, @Required String description,
            @Required Long[] speakers, @Required Track track) {
        Logger.info("Tentative d'enregistrement de la session " + title);
        Session talk;
        if (id == null) {
            talk = new Session();
        } else {
            talk = Session.findById(id);
        }
        talk.title = title;
        talk.summary = summary;
        talk.description = description;
        talk.track = track;
        Set<Speaker> speakerEntities = new HashSet<Speaker>(speakers.length);
        for (Long speakerId : speakers) {
            Speaker speakerEntity = Speaker.findById(speakerId);
            speakerEntities.add(speakerEntity);
        }
        talk.updateSpeakers(speakerEntities);
        if (validation.hasErrors()) {
            Logger.error(validation.errors().toString());
            render("Sessions/edit.html", talk);
        }
        talk.save();
        flash.success("Session " + talk + " enregistrée");
        Logger.info("Session " + talk + " enregistrée");
        show(talk.id);
    }
}