package controllers;

import java.util.List;
import models.LinkItEmail;
import models.Member;
import models.Staff;
import play.Logger;
import play.data.validation.Validation;

public class Infos extends PageController {

    public static void orga() {
        render("Infos/orga.html");
    }

    public static void faq() {
        render("Infos/faq.html");
    }

    public static void inscriptions() {
        render("Infos/inscriptions.html");
    }

    public static void kit() {
        render("Infos/kit.html");
    }

    public static void contacts() {
        render("Infos/contacts.html");
    }

    public static void acces() {
        render("Infos/acces.html");
    }

    public static void hotels() {
        render("Infos/hotels.html");
    }

    public static void sendStaff(LinkItEmail linkitemail) {
        Member from = Member.findByLogin(Security.connected());
        List<Member> members = Staff.findAll();
        linkitemail.recipients = members;
        linkitemail.from = from;
        validation.valid(linkitemail);
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            contacts();
        }
        linkitemail.send();
        flash.success("Merci pour votre email!");
        contacts();
    }
}