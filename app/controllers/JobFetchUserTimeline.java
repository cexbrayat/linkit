package controllers;

import helpers.TransactionCallbackWithoutResult;
import helpers.TransactionTemplate;
import java.util.Collection;
import models.Account;
import models.Member;
import models.activity.StatusActivity;
import play.Logger;
import play.db.jpa.NoTransaction;
import play.jobs.Job;

/**
 * Asynchronous fetch of one user timelines on external providers (Google+, Twitter)
 * @author Sryl <cyril.lacote@gmail.com>
 */
@NoTransaction
public class JobFetchUserTimeline extends Job {

    private Member member;
    private Collection<Account> accounts;
    
    private TransactionTemplate txTemplate = new TransactionTemplate();

    public JobFetchUserTimeline(Member member) {
        this.member = member;
        this.accounts = member.getOrderedAccounts();
    }
    
    @Override
    public void doJob() {
        Logger.info("BEGIN JOB JobFetchUserTimeline for member %s", member);
        for (final Account account : accounts) {
            txTemplate.execute(new TransactionCallbackWithoutResult() {
                public void doInTransaction() {
                    StatusActivity.fetchForAccount(account.id);
                }
            });
        }
        Logger.info("END JOB JobFetchUserTimeline for member %s", member);
    }
}
