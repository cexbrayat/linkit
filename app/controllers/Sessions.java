package controllers;

import helpers.JavaExtensions;
import helpers.Lists;
import play.*;

import java.util.*;
import models.SessionComment;
import models.Member;
import models.Role;
import models.Session;
import models.Staff;
import models.Talk;
import org.apache.commons.lang.StringUtils;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.libs.Images;

public class Sessions extends PageController {

    public static void index() {
        List<Talk> sessions = null;
        if (Security.check(Role.ADMIN_SESSION)) {
            sessions = Talk.findAll();
        } else {
            sessions = Talk.findAllValidated();
        }
        Collections.shuffle(sessions);
        Logger.info(sessions.size() + " sessions");
        render("Sessions/list.html", sessions);
    }

    public static void create(final String speakerLogin) throws Throwable {
        SecureLinkIt.checkAccess(); // Connected
        
        Member speaker = Member.findByLogin(speakerLogin);
        Talk talk = new Talk();
        if (speaker != null) {
            talk.addSpeaker(speaker);
        }
        List<Member> speakers = speakersFor(talk, speaker);
        render("Sessions/edit.html", talk, speakers);
    }

    private static void checkAccess(Session talk) throws Throwable {
        // No need to be 
        // SecureLinkIt.checkAccess();
        Member user = Member.findByLogin(Security.connected());
        if (talk.valid || (user instanceof Staff) || talk.hasSpeaker(Security.connected())) {
            // alright!
        } else {
            flash.error("Vous n'avez pas accès à cette fonctionnalité");
            index();
        }
    }

    public static void edit(final Long sessionId) throws Throwable {
        Session talk = Session.findById(sessionId);
        notFoundIfNull(talk);
        checkAccess(talk);

        Member member = Member.findByLogin(Security.connected());
        List<Member> speakers = speakersFor(talk, member);
 
        render(talk, speakers);
    }

    public static void delete(final Long sessionId) throws Throwable {
        Session talk = Session.findById(sessionId);
        notFoundIfNull(talk);
        checkAccess(talk);

        talk.delete();
        index();
    }
    
    private static List<Member> speakersFor(Session talk, Member member) {
        List<Member> speakers = Member.findAll();
        // Put actual speakers in top of list
        List<Member> actualSpeakers = new ArrayList<Member>(talk.speakers);
        Collections.sort(actualSpeakers);
        speakers = Lists.putOnTop(speakers, actualSpeakers);
        // Put current user on top.
        if (member != null) {
            speakers = Lists.putOnTop(speakers, member);
        }
        return speakers;
    }
    
    public static void show(final Long sessionId, String slugify, boolean noCount) throws Throwable {
        Session talk = Session.findById(sessionId);
        notFoundIfNull(talk);
        checkAccess(talk);

        // Don't count look when coming from internal redirect
        if (!noCount) {
            talk.lookedBy(Member.findByLogin(Security.connected()));
        }
        render(talk);
    }

    public static void postComment(
            Long talkId,
            @Required String content) throws Throwable {
        Session talk = Session.findById(talkId);
        notFoundIfNull(talk);

        if (Validation.hasErrors()) {
            render("Sessions/show.html", talk);
        }

        Member author = Member.findByLogin(Security.connected());
        talk.addComment(new SessionComment(author, talk, content));
        talk.save();
        flash.success("Merci pour votre commentaire %s", author);
        show(talkId, JavaExtensions.slugify(talk.title), true);
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText();
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

    public static void save(@Valid Talk talk, String[] interests, String newInterests) throws Throwable {
        Logger.info("Tentative d'enregistrement de la session " + talk);
        if (interests != null) {
            talk.updateInterests(interests);
        }
        if (newInterests != null) {
            talk.addInterests(StringUtils.splitByWholeSeparator(newInterests, ","));
        }
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            flash.error(Messages.get("validation.errors"));
            List<Member> speakers = speakersFor(talk, Member.findByLogin(Security.connected()));
            render("Sessions/edit.html", talk, speakers);
        }
        talk.update();
        flash.success("Session " + talk + " enregistrée");
        Logger.info("Session " + talk + " enregistrée");
        show(talk.id, JavaExtensions.slugify(talk.title), true);
    }
    
    public static void validate(long talkId) throws Throwable {
        Talk talk = Talk.findById(talkId);
        notFoundIfNull(talk);

        talk.validate();
        show(talkId, JavaExtensions.slugify(talk.title), true);
    }
    
    public static void unvalidate(long talkId) throws Throwable {
        Talk talk = Talk.findById(talkId);
        notFoundIfNull(talk);

        talk.unvalidate();
        show(talkId, JavaExtensions.slugify(talk.title), true);
    }
}