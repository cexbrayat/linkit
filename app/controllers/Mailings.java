package controllers;

import java.util.List;
import models.Email;
import models.Member;
import models.Role;
import play.Logger;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.mvc.With;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Check(Role.ADMIN_MEMBER)
@With(Secure.class)
public class Mailings extends PageController {

    public static void index() {
        List<Email> mailings = Email.findAll();
        render(mailings);
    }
    
    public static void create() {
        render("Mailings/edit.html", new Email());
    }
    
    public static void edit(long mailingId) {
        Email mailing = Email.findById(mailingId);
        render("Mailings/edit.html", mailing);
    }
    
    public static void save(Email mailing) {
        mailing.from = Member.findByLogin(Security.connected());
        validation.valid(mailing);
        if (Validation.hasErrors()) {
            Logger.error(Validation.errors().toString());
            flash.error(Messages.get("validation.errors"));
            render("Mailings/edit.html", mailing);
        }
        mailing.save();
        flash.success("Mailing enregistré : %s", mailing);
        index();
    }
    
    public static void send(long mailingId) {
        Email mailing = Email.findById(mailingId);
        mailing.send();
        flash.success("Le mailing \"%s\" est en cours d'envoi", mailing);
        index();
    }
}
