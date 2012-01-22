package controllers;

import java.util.List;
import models.mailing.Mailing;
import models.Member;
import models.Staff;
import models.mailing.MailingStatus;
import models.mailing.MembersSet;
import play.Logger;
import play.data.validation.Validation;
import play.i18n.Messages;

public class Infos extends PageController {

    public static void about() {
        render();
    }

    public static void faq() {
        render();
    }

    public static void kit() {
        render();
    }

    public static void contact() {
        render();
    }

    public static void acces() {
        render();
    }

    public static void hotels() {
        render();
    }

    public static void sendStaff(Mailing email) {
        Member from = Member.findByLogin(Security.connected());
        List<Member> staff = Staff.findAll();
        email.recipients = MembersSet.Staff;
        email.from = from;
        validation.valid(email);
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            flash.error(Messages.get("validation.errors"));
            validation.keep();
            contact();
        }
        email.save();
        Mails.contact(email);
        email.status = MailingStatus.Sent;
        email.save();
        flash.success("Merci pour votre email!");
        contact();
    }
}