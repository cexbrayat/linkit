package models;

import javax.persistence.Entity;
import org.apache.commons.lang.StringUtils;
import play.data.validation.Required;

/**
 * A Twitter account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class TwitterAccount extends OAuthAccount {
    
    @Required
    public Long userId;         // 217990448
    @Required
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
            if (StringUtils.isBlank(member.twitterName)) member.twitterName = this.screenName;
            if (StringUtils.isBlank(member.displayName)) member.displayName = this.name;
        }
    }

    @Override
    public Member findCorrespondingMember() {
        return Member.find("byTwitterName", screenName).first();
    }
}
