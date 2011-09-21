package models;

import javax.persistence.Entity;

/**
 * An OAuth account on a third-party authentication provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class TwitterAccount extends OAuthAccount {
    
    public Long userId;
    public String screenName;

    public TwitterAccount() {
        super(ProviderType.Twitter);
    }
    
    @Override
    public String toString(){
        return "provider {" + provider + "}, screenName {" + screenName + "}";
    }

    @Override
    public String getOAuthLogin() {
        return screenName;
    }
}
