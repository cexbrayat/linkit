package models.auth;

import javax.persistence.Entity;

import models.ProviderType;

import org.apache.commons.codec.binary.Base64;

import play.Play;
import play.libs.Crypto;
import play.libs.Crypto.HashType;

/**
 * An account on Link-IT (basic login/password local authentication)
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LinkItAccount extends AuthAccount {

    public String password;

    public LinkItAccount(String password) {
        super(ProviderType.LinkIt);
        this.password = hashPassword(password)          ;
    }
    
    @Override
    public String toString(){
        return "Link-IT account for " + member;
    }

    @Override
    public void initMemberProfile() {
        // Nothing;
    }

    public static String hashPassword(String password) {
        // Retrieve salt from configuration & create hashed password
        String salt = Play.configuration.get("application.salt").toString();
        return Crypto.passwordHash(password + salt, HashType.SHA256);
    }

    public void updatePassword(String password) {
        String newPasswordHash = hashPassword(password);
        this.password = newPasswordHash;
        save();
    }
}
