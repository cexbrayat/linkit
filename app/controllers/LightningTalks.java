package controllers;

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

public class  LightningTalks extends PageController {

    public static void list() {
        List<LightningTalk> sessions = LightningTalk.findAll();

        if(JSON.equals(request.format))
        {
            //TODO JRI add myVote
            renderJSON(sessions, new LightningTalkSerializer());
        }

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
    
    public static void show(final Long id)
    {
        show(id, false);
    }

    public static void show(final Long sessionId, boolean noCount) {
        LightningTalk talk = LightningTalk.findById(sessionId);
        notFoundIfNull(talk);
        // Don't count look when coming from internal redirect
        if (!noCount) {
            talk.lookedBy(Member.findByLogin(Security.connected()));
        }

        if(JSON.equals(request.format))
        {
            renderJSON(talk, new LightningTalkSerializer());
        }

        List<Activity> activities = Activity.recentsBySession(talk, 1, 5);
        render(talk, activities);
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
        flash.success("LightningTalk " + talk + " enregistré");
        Logger.info("LightningTalk " + talk + " enregistré");
        show(talk.id, true);
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
        show(id, true);
    }

    public static void delete(final Long sessionId) {
        LightningTalk talk = LightningTalk.findById(sessionId);
        talk.delete();
        list();
    }
}