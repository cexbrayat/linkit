package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * An OAuth account on a third-party authentication provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Account extends Model {
    
    @Required
    @ManyToOne(optional = false)
    public Member member;

    // Authentication provider : linkit, twitter, google, ...
    @Required
    public String provider;
    
    public Long userId;
    public String login;
    
    /** For basic (Link-IT) authentication */
    public String password;

    /** For OAuth authentication */
    public String secret;
    public String token;

    public Account(String provider) {
        this.provider = provider;
    }
    
    @Override
    public String toString(){
        return "provider {" + provider + "}, login {" + login + "}";
    }
}
