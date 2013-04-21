package controllers;

import models.Member;
import models.Role;
import models.mailing.Mailing;
import play.Logger;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.mvc.With;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Check(Role.ADMIN_MEMBER)
@With(SecureLinkIt.class)
public class Mailings extends PageController {

    public static void index() {
        List<Mailing> mailings = Mailing.findAll();
        render(mailings);
    }
    
    public static void create() {
        render("Mailings/edit.html", new Mailing());
    }
    
    public static void edit(long mailingId) {
        Mailing mailing = Mailing.findById(mailingId);
        render(mailing);
    }
    
    public static void show(long mailingId) {
        Mailing mailing = Mailing.findById(mailingId);
        render(mailing);
    }
    
    public static void preview(long mailingId) {
        Mailing mailing = Mailing.findById(mailingId);
        render("Mails/mailing.html", mailing);
    }
    
    public static void save(Mailing mailing) {
        mailing.from = Member.findByLogin(Security.connected());
        mailing.sentAt = new Date();
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
        Mailing mailing = Mailing.findById(mailingId);
        mailing.send();
        flash.success("La demande d'envoi du mailing \"%s\" a bien été enregistré. Il partira dans la nuit à un premier lot de destinataires.", mailing);
        index();
    }
    
    public static void delete(long mailingId) {
        Mailing mailing = Mailing.findById(mailingId);
        if (mailing.isUpdatable()) {
            mailing.delete();
            flash.success("Le mailing \"%s\" a été supprimé.", mailing);
        } else {
            flash.error("Le mailing \"%s\" ne peut pas être supprimé : son envoi a déjà commencé", mailing);
        }
        index();
    }
}
