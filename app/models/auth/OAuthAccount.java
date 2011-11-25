package models.auth;

import javax.persistence.Entity;
import models.Member;
import models.ProviderType;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * An OAuth account on a third-party authentication provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public abstract class OAuthAccount extends AuthAccount {

    @Required
    public String token;
    @Required
    public String secret;
    
    public OAuthAccount(ProviderType provider, String token, String secret) {
        super(provider);
        this.token = token;
        this.secret = secret;
    }

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
