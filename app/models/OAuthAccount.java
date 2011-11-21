package models;

import javax.persistence.Entity;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * An OAuth account on a third-party authentication provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public abstract class OAuthAccount extends Model {

    @Required
    public String token;
    @Required
    public String secret;
    
    public OAuthAccount(ProviderType provider, String token, String secret) {
        super();
        this.token = token;
        this.secret = secret;
    }
    
    public abstract Account getAccount();
    
    /**
     * Initialize member profile from account data
     */
    public abstract void initMemberProfile();

    /**
     * Template method to retrieve login value
     * @return Account property value to use as login for Link-IT
     */
    public abstract String getOAuthLogin();

    /**
     * Find an existing account for this authentication account
     * @return Corresponding account found, null if not
     */
    public abstract Member findCorrespondingMember();
}
