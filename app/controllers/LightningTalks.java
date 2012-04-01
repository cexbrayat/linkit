package controllers;

import helpers.JavaExtensions;
import models.*;
import models.serialization.LightningTalkSerializer;
import models.serialization.SessionCommentSerializer;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.data.validation.Valid;
import play.data.validation.Validation;

import java.util.List;
import models.activity.Activity;
import play.data.validation.Required;
import play.i18n.Messages;

public class LightningTalks extends PageController {

    public static void list() {
        List<LightningTalk> sessions = LightningTalk.findAll();

        if(JSON.equals(request.format))
        {
            Member member = Member.findByLogin(Security.connected());
            renderJSON(sessions, new LightningTalkSerializer(member));
        }

        render(sessions);
    }

    public static void create() throws Throwable {
        SecureLinkIt.checkAccess(); // Connected
        LightningTalk talk = new LightningTalk();
        render("LightningTalks/edit.html", talk);
    }

    public static void edit(final Long sessionId) throws Throwable {
        LightningTalk talk = LightningTalk.findById(sessionId);
        notFoundIfNull(talk);
        checkAccess(talk);

        render(talk);
    }
    
    public static void show(final Long id)
    {
        show(id, "", false);
    }

    private static void checkAccess(Session talk) throws Throwable {
        SecureLinkIt.checkAccess();
        Member user = Member.findByLogin(Security.connected());
        if (!(user instanceof Staff || talk.hasSpeaker(user.login))) {
            unauthorized();
        }
    }

    public static void show(final Long sessionId, String slugify, boolean noCount) {
        LightningTalk talk = LightningTalk.findById(sessionId);
        notFoundIfNull(talk);
        // Don't count look when coming from internal redirect
        if (!noCount) {
            talk.lookedBy(Member.findByLogin(Security.connected()));
        }

        List<Activity> activities = Activity.recentsBySession(talk, 1, 5);
        render(talk, activities);
    }
    
    public static void showSession(final Long id)    {
        LightningTalk talk = LightningTalk.findById(id);
        notFoundIfNull(talk);
        if(JSON.equals(request.format))
        {
            Member member = Member.findByLogin(Security.connected());
            renderJSON(talk, new LightningTalkSerializer(member));
        }
    }

    public static void save(LightningTalk talk, String[] interests, String newInterests) {
        Member member = Member.findByLogin(Security.connected());
        talk.addSpeaker(member);
        if (interests != null) {
            talk.updateInterests(interests);
        }
        if (newInterests != null) {
            talk.addInterests(StringUtils.splitByWholeSeparator(newInterests, ","));
        }
        validation.valid(talk);
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            flash.error(Messages.get("validation.errors"));
            render("LightningTalks/edit.html", talk);
        }
        talk.update();
        flash.success("LightningTalk %s enregistré", talk);
        Logger.info("LightningTalk %s enregistré", talk);
        show(talk.id, JavaExtensions.slugify(talk.title), true);
    }

    public static void listComments(final Long id) {
        LightningTalk talk = LightningTalk.findById(id);
        notFoundIfNull(talk);
        if (JSON.equals(request.format)) {
            renderJSON(talk.comments, new SessionCommentSerializer());
        }
    }

    @Check("member")
    public static void postComment(
            Long id,
            @Required String content) {

        LightningTalk talk = LightningTalk.findById(id);
        if (Validation.hasErrors()) {
            render("LightningTalks/show.html", talk);
        }

        Member author = Member.findByLogin(Security.connected());
        talk.addComment(new SessionComment(author, talk, content));
        talk.save();
        flash.success("Merci pour votre commentaire %s", author);
        show(id, "", true);
    }

    public static void delete(final Long sessionId) {
        LightningTalk talk = LightningTalk.findById(sessionId);
        talk.delete();
        list();
    }
}