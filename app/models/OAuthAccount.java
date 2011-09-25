package models;

import javax.persistence.Entity;

/**
 * An OAuth account on a third-party authentication provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public abstract class OAuthAccount extends Account {

    public String token;
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
}
