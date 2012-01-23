package controllers;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import models.Member;
import models.mailing.Mailing;
import models.mailing.MailingStatus;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.On;

/**
 * Asynchronous batch sending of mailings.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@On("0 0 3 * * ?")  // 3H du matin chaque jour
public class JobSendMailings extends Job {

    private final int dailyBatchSize = Integer.valueOf(Play.configuration.getProperty("linkit.mail.dailyBatchSize"));
    
    @Override
    public void doJob() {
        
        Logger.info("BEGIN JobSendMailings");
        
        int nbSends = 0;
        
        List<Mailing> pendings = Mailing.pending();
        Logger.info("JobSendMailings : %d mailings en attente", pendings.size());
        
        // Pour chaque mailing en attente
        Iterator<Mailing> iPendingMailing = pendings.iterator();
        while (nbSends < dailyBatchSize && iPendingMailing.hasNext()) {
            final Mailing mailing = iPendingMailing.next();
            
            Set<Member> recipients = mailing.getPendingRecipients();
            Logger.info("JobSendMailings : %d membres en attente du mailing %s", recipients.size(), mailing);

            Iterator<Member> iRecipient = recipients.iterator();
            while (nbSends < dailyBatchSize && iRecipient.hasNext()) {
                final Member recipient = iRecipient.next();
                
                Logger.info("JobSendMailings : envoi du mailing %s au membre %s", mailing, recipient);
                Mails.mailing(mailing, recipient);
                mailing.addActualRecipient(recipient);
                nbSends++;
            }
            
            if (!iRecipient.hasNext()) {
                // Si nous avons épuisé tous les destinataires en attente
                // C'est que le mailing a été complètement envoyé
                mailing.status = MailingStatus.Sent;
                Logger.info("JobSendMailings : mailing %s complètement envoyé!", mailing);
            }
            mailing.save();
        }

        if (nbSends >= dailyBatchSize) {
            Logger.info("JobSendMailings : quota quotidien de %d envois atteint, on stoppe les envois", dailyBatchSize);
        }
        Logger.info("END JobSendMailings");
    }
}
