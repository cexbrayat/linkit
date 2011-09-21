package models;

import javax.persistence.Entity;

/**
 * An OAuth account on a third-party authentication provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public abstract class OAuthAccount extends Account {

    public String secret;
    public String token;

    public OAuthAccount(ProviderType provider) {
        super(provider);
    }

    /**
     * Template method to retrieve login value
     * @return Account property value to use as login for Link-IT
     */
    public abstract String getOAuthLogin();
}
