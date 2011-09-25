package models;

import javax.persistence.Entity;

/**
 * An OAuth account on a third-party authentication provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class TwitterAccount extends OAuthAccount {
    
    public Long userId;         // 217990448
    public String screenName;   // clacote
    public String lang;         // en
    public String name;         // Cyril Lacôte
    public String location;     // In the bright side
    public String profileImageUrl;  // http://a2.twimg.com/profile_images/1171939364/image_normal.jpg
    public Long statusesCount;  // 491
    public Long friendsCount;   // 159

    public TwitterAccount(String token, String secret) {
        super(ProviderType.Twitter, token, secret);
    }
    
    @Override
    public String toString(){
        return "provider {" + provider + "}, screenName {" + screenName + "}";
    }

    @Override
    public String getOAuthLogin() {
        return screenName;
    }

    @Override
    public void initMemberProfile() {
        if (member != null) {
            member.twitterName = this.screenName;
            member.displayName = this.name;
        }
    }
}
