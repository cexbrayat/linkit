package controllers;

import models.*;
import models.mailing.Mailing;
import models.mailing.MailingStatus;
import models.mailing.MembersSet;
import play.Logger;
import play.data.validation.Validation;

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
        render(email);
    }
    public static void acces() {
        render();
    }

    public static void hotels() {
        render();
    }

    public static void mixit12() {
        ConferenceEvent event = ConferenceEvent.mixit12;
        List<Talk> talks = Talk.findAllValidatedOn(event);
        List<Member> speakers = Talk.findAllSpeakersOn(event);
        List<LightningTalk> lightningTalks = LightningTalk.findAllOn(event);
        render(event, talks, speakers, lightningTalks);
    }

    public static void send(Mailing email) {
        Member from = Member.findByLogin(Security.connected());
        List<Member> staff = Staff.findAll();
        email.recipients = MembersSet.Staff;
        email.from = from;
        validation.valid(email);
        validation.required("email.email", email.email);
        validation.email("email.email", email.email);
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            render("Infos/contact.html", email);
        }
        email.save();
        Mails.contact(email);
        email.actualRecipients.addAll(staff);
        email.status = MailingStatus.Sent;
        email.save();
        flash.success("Votre message a bien été envoyé!");
        Application.index();
    }
}