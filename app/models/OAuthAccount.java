package models;

import javax.persistence.Entity;
import play.data.validation.Required;

/**
 * An OAuth account on a third-party authentication provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public abstract class OAuthAccount extends Account {

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
     * Find an existing member wich could own this account
     * @return Corresponding member found, null if not
     */
    public abstract Member findCorrespondingMember();
}
