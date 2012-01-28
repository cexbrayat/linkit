package controllers;

import helpers.TransactionCallback;
import helpers.TransactionCallbackWithoutResult;
import helpers.TransactionTemplate;
import java.util.List;
import models.Account;
import models.activity.StatusActivity;
import play.Logger;
import play.Play;
import play.db.jpa.NoTransaction;
import play.jobs.Every;
import play.jobs.Job;

/**
 * Asynchronous fetch of user timelines on external providers (Google+, Twitter)
 * @author Sryl <cyril.lacote@gmail.com>
 */
//@Every("1h") // CLA 14/12/2011 : avoid Twitter blacklisting if we make more than 150 requests / hour
@NoTransaction
public class JobFetchAllUsersTimelines extends Job {

    // Read-only template
    private TransactionTemplate txTemplate = new TransactionTemplate(true);
    
    @Override
    public void doJob() {        
        if (!"test".equals(Play.id)) {
            Logger.info("BEGIN fetch timelines");

            List<Long> accountsId = txTemplate.execute(new TransactionCallback() {
                public List<Long> doInTransaction() {
                    return Account.findAllIds();
                }
            });

            // Not read-only transaction
            txTemplate.setReadOnly(false);
            for (final Long id : accountsId) {

                txTemplate.execute(new TransactionCallbackWithoutResult() {
                    public void doInTransaction() {
                        StatusActivity.fetchForAccount(id);
                    }
                });
            }
            Logger.info("END fetch timelines");
        }
    }
}
