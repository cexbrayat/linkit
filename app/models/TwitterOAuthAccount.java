package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.apache.commons.lang.StringUtils;
import play.data.validation.Required;

/**
 * A Twitter account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class TwitterOAuthAccount extends OAuthAccount {
    
    @ManyToOne(optional=false)
    public TwitterAccount account;

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
    
    public TwitterOAuthAccount(String token, String secret) {
        super(ProviderType.Twitter, token, secret);
        account = new TwitterAccount();
    }

    @Override
    public String getOAuthLogin() {
        return screenName;
    }

    @Override
    public void initMemberProfile() {
        if (account != null) {
            if (StringUtils.isBlank(account.screenName)) account.screenName = this.screenName;
            if (account.member != null) {
                if (StringUtils.isBlank(account.member.displayName)) account.member.displayName = this.name;
            }
        }
    }

    @Override
    public Member findCorrespondingMember() {
        return TwitterAccount.findMemberByScreenName(this.screenName);
    }

    @Override
    public Account getAccount() {
        return account;
    }
}
