package models;

import javax.persistence.Entity;
import org.apache.commons.lang.StringUtils;
import play.data.validation.Required;

/**
 * A Google account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class GoogleAccount extends OAuthAccount {
    
    public String googleId;     // 114128610730314333831
    @Required
    public String email;        // cyril.lacote@gmail.com
    public String name;         // Cyril Lacôte
    public String givenName;    // Cyril
    public String familyName;   // Lacôte
    public String link;         // http://profiles.google.com/cyrillacote
    public String picture;      // https://lh5.googleusercontent.com/-ZoXanD5pbxM/AAAAAAAAAAI/AAAAAAAANks/ECGcSElQ6hM/photo.jpg
    public String gender;       // male
    public String birthday;     // 0000-03-26 (yes, 0000 for me?!)
    public String locale;       // en
    
    public GoogleAccount(String token, String secret) {
        super(ProviderType.Google, token, secret);
    }
    
    @Override
    public String toString(){
        return "provider {" + provider + "}, name {" + name + "}";
    }

    @Override
    public String getOAuthLogin() {
        return email;
    }

    @Override
    public void initMemberProfile() {
        if (member != null) {
            if (StringUtils.isBlank(member.googlePlusId)) member.googlePlusId = this.googleId;
            if (StringUtils.isBlank(member.email)) member.email = this.email;
            if (StringUtils.isBlank(member.firstname)) member.firstname = this.givenName;
            if (StringUtils.isBlank(member.lastname)) member.lastname = this.familyName;
            if (StringUtils.isBlank(member.displayName)) member.displayName = this.name;
        }
    }

    @Override
    public Member findCorrespondingMember() {
        return Member.find("byEmail", getOAuthLogin()).first();
    }
}
