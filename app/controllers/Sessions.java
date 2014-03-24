package controllers;

import helpers.Booleans;
import helpers.JavaExtensions;
import helpers.Lists;
import models.*;
import models.planning.PlanedSlot;
import models.planning.Planning;
import models.planning.Slot;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.libs.Images;
import play.mvc.With;

import java.util.*;

public class Sessions extends PageController {

    private static final boolean CFP_OPEN = Boolean.valueOf(Play.configuration.getProperty("cfp.isOpen", "false"));

    public static void index() {
        listOn(ConferenceEvent.CURRENT);
    }

    public static void planningMixIT13() {
        Planning planning = PlanedSlot.on(ConferenceEvent.mixit13, true);
        renderTemplate("Sessions/planningMixIT13.html", planning);
    }

    public static void listOn(ConferenceEvent event) {
        List<Talk> sessions = null;
        if (Security.check(Role.ADMIN_SESSION)) {
            sessions = Talk.findAllOn(event);
        } else {
            sessions = Talk.findAllValidatedOn(event);
        }
        Collections.shuffle(sessions);
        Logger.info(sessions.size() + " sessions");
        render("Sessions/list.html", sessions, event);
    }

    public static void create(final String speakerLogin, boolean briefed) throws Throwable {
        if (!(CFP_OPEN || Security.check(Role.ADMIN_SESSION))) {
         // forbidden("Damned! The Call For Paper is closed!!!");
        }

        SecureLinkIt.checkAccess(); // Connected

        if (briefed) {
            Member speaker = Member.findByLogin(speakerLogin);
            Talk talk = new Talk();
            if (speaker != null) {
                talk.addSpeaker(speaker);
            }
            // Fix #133
            Member user = Member.findByLogin(Security.connected());
            if (user != null) {
                talk.addSpeaker(user);
            }
            List<Member> speakers = speakersFor(talk, speaker);
            render("Sessions/edit.html", talk, speakers);
        } else {
            render("Sessions/cfp-briefing.html", speakerLogin);
        }
    }

    private static void checkReadAccess(Session talk) {
        // No need to be 
        // SecureLinkIt.checkAccess();
        Member user = Member.findByLogin(Security.connected());
        if (talk.valid || (user instanceof Staff) || talk.hasSpeaker(user)) {
            // alright!
        } else {
            flash.error("Vous n'avez pas officiellement accès à cette session, coquin!");
            index();
        }
    }

    private static void checkWriteAccess(Session talk) {
        Member user = Member.findByLogin(Security.connected());
        if ((user instanceof Staff) || talk.speakers.isEmpty() || talk.hasSpeaker(user)) {
            // alright!
        } else {
            flash.error("Vous n'avez pas (ou plus :p) accès à la modification de cette session, coquin!");
            index();
        }
    }

    public static void edit(final Long sessionId) throws Throwable {
        Session talk = Session.findById(sessionId);
        notFoundIfNull(talk);
        checkWriteAccess(talk);

        Member member = Member.findByLogin(Security.connected());
        List<Member> speakers = speakersFor(talk, member);
 
        render(talk, speakers);
    }

    public static void delete(final Long sessionId) throws Throwable {
        Session talk = Session.findById(sessionId);
        notFoundIfNull(talk);
        checkWriteAccess(talk);

        talk.delete();
        index();
    }
    
    private static List<Member> speakersFor(Session talk, Member member) {
        List<Member> speakers = Member.findAll();
        // Put actual speakers in top of list
        List<Member> actualSpeakers = new ArrayList<Member>(talk.speakers);
        Collections.sort(speakers);
        speakers = Lists.putOnTop(speakers, actualSpeakers);
        // Put current user on top.
        if (member != null) {
            speakers = Lists.putOnTop(speakers, member);
        }
        return speakers;
    }
    
    public static void show(final Long sessionId, String slugify, boolean noCount) {
        Talk talk = Talk.findById(sessionId);
        notFoundIfNull(talk);
        checkReadAccess(talk);

        PlanedSlot planedSlot = PlanedSlot.forTalkOn(talk, ConferenceEvent.CURRENT);
        Slot slot = planedSlot != null ? planedSlot.slot : null;

        List<Member> voters = Vote.findVotersBySession(talk);
        Collections.shuffle(voters);

        Member user = Member.findByLogin(Security.connected());
        boolean showPrivateComments = user != null && user.canSeePrivateCommentsOf(talk);

        // Don't count look when coming from internal redirect
        if (!noCount) {
            talk.lookedBy(Member.findByLogin(Security.connected()));
        }
        
        render(talk, voters, slot, showPrivateComments);
    }

    public static void postComment(
            Long talkId,
            Boolean privatelyVisible,
            @Required String content) {
        Session talk = Session.findById(talkId);
        notFoundIfNull(talk);

        if (Validation.hasErrors()) {
            render("Sessions/show.html", talk);
        }

        Member author = Member.findByLogin(Security.connected());
        SessionComment comment = new SessionComment(author, talk, content);
        comment.privatelyVisible = Booleans.valueOrFalse(privatelyVisible);
        talk.addComment(comment);
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

    public static void save(Talk talk, @Required @MinSize(1) Long[] speakerIds, String[] interests, String newInterests) throws Throwable {
        checkWriteAccess(talk);
        Logger.info("Tentative d'enregistrement de la session %s", talk);

        if (speakerIds != null) {
            List<Member> newSpeakers = new ArrayList<Member>(speakerIds.length);
            for (Long id : speakerIds) {
                Member speaker = Member.findById(id);
                if (speaker != null) {
                    newSpeakers.add(speaker);
                }
            }
            talk.updateSpeakers(newSpeakers);
        }
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
            List<Member> speakers = speakersFor(talk, Member.findByLogin(Security.connected()));
            render("Sessions/edit.html", talk, speakers);
        }
        talk.update();
        flash.success("Session %s enregistrée", talk);
        Logger.info("Session %s enregistrée", talk);
        show(talk.id, JavaExtensions.slugify(talk.title), true);
    }

    public static void validate(long talkId) throws Throwable {
        Talk talk = Talk.findById(talkId);
        notFoundIfNull(talk);
        checkWriteAccess(talk);

        talk.validate();
        show(talkId, JavaExtensions.slugify(talk.title), true);
    }

    public static void unvalidate(long talkId) throws Throwable {
        Talk talk = Talk.findById(talkId);
        notFoundIfNull(talk);
        checkWriteAccess(talk);

        talk.unvalidate();
        show(talkId, JavaExtensions.slugify(talk.title), true);
    }

    public static void feedback(long talkId, boolean feedback) throws Throwable {
        Talk talk = Talk.findById(talkId);
        notFoundIfNull(talk);
        checkWriteAccess(talk);

        talk.feedback = feedback;
        talk.save();

        show(talkId, JavaExtensions.slugify(talk.title), true);
    }
}