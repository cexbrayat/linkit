package models.auth;

import javax.persistence.Entity;
import models.GoogleAccount;
import models.Member;
import models.ProviderType;
import org.apache.commons.lang.StringUtils;

/**
 * A Google account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class GoogleOAuthAccount extends OAuthAccount {
    
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
    }

    @Override
    public void initMemberProfile() {
        if (member != null) {
            if (StringUtils.isBlank(member.email)) member.email = this.email;
            if (StringUtils.isBlank(member.firstname)) member.firstname = this.givenName;
            if (StringUtils.isBlank(member.lastname)) member.lastname = this.familyName;
            if (StringUtils.isBlank(member.login)) member.login = getOAuthLogin();
            GoogleAccount account = member.getGoogleAccount();
            if (account == null) {
                account = new GoogleAccount(googleId);
                member.addAccount(account);
            } else {
                if (StringUtils.isBlank(account.googleId)) account.googleId = this.googleId;
            }
        }
    }

    @Override
    public Member findCorrespondingMember() {
        return Member.findByEmail(this.email);
    }

    @Override
    public String getOAuthLogin() {
        return StringUtils.substringBefore(email, "@");
    }
}
