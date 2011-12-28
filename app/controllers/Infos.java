package controllers;

import java.util.List;
import models.LinkItEmail;
import models.Member;
import models.Staff;
import play.Logger;
import play.data.validation.Validation;

public class Infos extends PageController {

    public static void about() {
        render("Infos/about.html");
    }

    public static void faq() {
        render("Infos/faq.html");
    }

    public static void kit() {
        render("Infos/kit.html");
    }

    public static void contact() {
        render("Infos/contact.html");
    }

    public static void acces() {
        render("Infos/acces.html");
    }

    public static void hotels() {
        render("Infos/hotels.html");
    }

    public static void sendStaff(LinkItEmail email) {
        Member from = Member.findByLogin(Security.connected());
        List<Member> members = Staff.findAll();
        email.recipients = members;
        email.from = from;
        validation.valid(email);
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            flash.error("Des erreurs dans votre saisie!");
            validation.keep();
            contact();
        }
        email.send();
        flash.success("Merci pour votre email!");
        contact();
    }
}