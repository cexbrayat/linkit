package controllers;

import models.*;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.data.validation.Valid;
import play.data.validation.Validation;

import java.util.List;

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

   
}