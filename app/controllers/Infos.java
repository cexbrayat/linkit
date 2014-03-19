package controllers;

import models.*;
import models.mailing.Mailing;
import models.mailing.MailingStatus;
import models.mailing.MembersSet;
import play.Logger;
import play.cache.Cache;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.libs.Codec;
import play.libs.Images;

import java.util.List;

public class Infos extends PageController {

    public static void about() {
        render();
    }

    public static void faq() {
        render();
    }
    
    public static void inscription() {
        Member member = Member.findByLogin(Security.connected());
        render(member);
    }

    public static void kit() {
        render();
    }
    
    public static void contact() {
        Mailing email = new Mailing();
        email.from = Member.findByLogin(Security.connected());
        email.email = (email.from != null) ? email.from.email : null;

        String randomId = Codec.UUID();

        render(email, randomId);
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#000000", 5, "mixit");
        Cache.set(id, code, "2h");
        renderBinary(captcha);
    }

    public static void acces() {
        render();
    }

    public static void hotels() {
        render();
    }

    public static void codeOfConduct() {
        render();
    }

    public static void mixit12() {
        oldEvent(ConferenceEvent.mixit12);
    }

    private static void oldEvent(ConferenceEvent event) {
        List<Talk> talks = Talk.findAllValidatedOn(event);
        List<Member> speakers = Talk.findAllSpeakersOn(event);
        List<LightningTalk> lightningTalks = LightningTalk.findAllOn(event);
        render(event, talks, speakers, lightningTalks);
    }

    public static void mixit13() {
        oldEvent(ConferenceEvent.mixit13);
    }

    public static void send(Mailing email, @Required String captcha, String randomId) {
        Member from = Member.findByLogin(Security.connected());
        List<Member> staff = Staff.findAll();
        email.recipients = MembersSet.Staff;
        email.from = from;

        validation.valid(email);
        validation.required("email.email", email.email);
        validation.email("email.email", email.email);
        validation.equals(captcha, Cache.get(randomId)).message(Messages.get("captcha.error"));

        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            render("Infos/contact.html", email, randomId);
        }

        email.save();
        Mails.contact(email);
        email.actualRecipients.addAll(staff);
        email.status = MailingStatus.Sent;
        email.save();
        flash.success("Votre message a bien été envoyé!");

        Cache.delete(randomId);

        Application.index();
    }

	public static void mixteen() {
		render();
	}

}