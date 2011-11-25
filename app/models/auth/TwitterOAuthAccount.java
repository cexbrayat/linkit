package models.auth;

import javax.persistence.Entity;
import models.Member;
import models.ProviderType;
import models.TwitterAccount;
import org.apache.commons.lang.StringUtils;
import play.data.validation.Required;

/**
 * A Twitter account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class TwitterOAuthAccount extends OAuthAccount {

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
    }

    @Override
    public String getOAuthLogin() {
        return screenName;
    }

    @Override
    public void initMemberProfile() {
        if (member != null) {
            if (StringUtils.isBlank(member.displayName)) member.displayName = this.name;
            TwitterAccount account = member.getTwitterAccount();
            if (account == null) {
                account = new TwitterAccount(screenName);
                member.addAccount(account);
            } else {
                if (StringUtils.isBlank(account.screenName)) account.screenName = this.screenName;
            }
        }
    }

    @Override
    public Member findCorrespondingMember() {
        return TwitterAccount.findMemberByScreenName(this.screenName);
    }
}
