package models.activity;

import controllers.oauth.OAuthProvider;
import controllers.oauth.OAuthProviderFactory;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import models.Account;
import models.Member;
import models.OAuthAccount;
import models.ProviderType;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.IndexColumn;
import play.Logger;

/**
 * A status activity : someone ({@link Activity#member} posted a status on an external provider ({@link Activity#session}
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class StatusActivity extends Activity {

    @Lob
    public String content;
    public String url;
    
    /** ID of status on external provider */
    @IndexColumn(name="statusId_IDX", nullable=false)
    public String statusId;

    public StatusActivity(Member author, Date at, ProviderType provider, String content, String url, String statusId) {
        super(provider, at);
        this.member = author;
        this.provider = provider;
        this.content = StringUtils.substring(content, 0, 4000);
        this.url = url;
        this.statusId = statusId;
    }

    static public void fetchFor(Member member) {

        for (Account account : member.accounts) {
            if (account instanceof OAuthAccount) {
                OAuthAccount oAuthAccount = (OAuthAccount) account;
                OAuthProvider provider = OAuthProviderFactory.getProvider(account.provider);
                if (provider != null) {
                    Logger.info("Fetch timeline for %s on %s", member, account.provider);
                    List<StatusActivity> statuses = provider.fetchActivities(oAuthAccount);
                    if (!statuses.isEmpty()) {
                        // Memorizing most recent id
                        Collections.sort(statuses);
                        oAuthAccount.lastStatusId = statuses.get(0).statusId;
                        account.save();
                    }
                    
                    provider.enhance(statuses);
                    
                    for (StatusActivity status : statuses) {
                        boolean add = true;
                        // Google hack : workaround for lack of "since" parameter in API, returning already fetched statuses.
                        if (ProviderType.Google == account.provider) {
                            // We add this status only if we don't have it already in DB
                            long count = StatusActivity.count("provider = ? and statusId = ? ", account.provider, status.statusId);
                            add = (count <= 0l);
                        }
                        if (add) status.save();
                    }
                }
            }
        }
    }
    
    @Override
    public String getMessage(String lang) {
        return content;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
