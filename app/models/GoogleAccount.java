package models;

import javax.persistence.Entity;

/**
 * A Google account
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class GoogleAccount extends OAuthAccount {
    
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
            member.googlePlusId = this.googleId;
            member.email = this.email;
            member.firstname = this.givenName;
            member.lastname = this.familyName;
            member.displayName = this.name;
        }
    }
}
