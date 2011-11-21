package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.apache.commons.lang.StringUtils;
import play.data.validation.Required;

/**
 * A Google account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class GoogleOAuthAccount extends OAuthAccount {
    
    @ManyToOne(optional=false)
    public GoogleAccount account;
    
    public String googleId;     // 114128610730314333831
    public String email;        // cyril.lacote@gmail.com
    public String name;         // Cyril Lacôte
    public String givenName;    // Cyril
    public String familyName;   // Lacôte
    public String link;         // http://profiles.google.com/cyrillacote
    public String picture;      // https://lh5.googleusercontent.com/-ZoXanD5pbxM/AAAAAAAAAAI/AAAAAAAANks/ECGcSElQ6hM/photo.jpg
    public String gender;       // male
    public String birthday;     // 0000-03-26 (yes, 0000 for me?!)
    public String locale;       // en
    
    public GoogleOAuthAccount(String token, String secret) {
        super(ProviderType.Google, token, secret);
        account = new GoogleAccount();
    }

    @Override
    public void initMemberProfile() {
        if (account != null) {
            if (StringUtils.isBlank(account.googleId)) account.googleId = this.googleId;
            if (account.member != null) {
                if (StringUtils.isBlank(account.member.email)) account.member.email = this.email;
                if (StringUtils.isBlank(account.member.firstname)) account.member.firstname = this.givenName;
                if (StringUtils.isBlank(account.member.lastname)) account.member.lastname = this.familyName;
                if (StringUtils.isBlank(account.member.displayName)) account.member.displayName = this.name;
            }
        }
    }

    @Override
    public Member findCorrespondingMember() {
        return Member.find("byEmail", this.email).first();
    }

    @Override
    public String getOAuthLogin() {
        return email;
    }

    @Override
    public Account getAccount() {
        return account;
    }
}
