package models;

import javax.persistence.Entity;

/**
 * An account on Link-IT (basic login/password local authentication)
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LinkItAccount extends Account {

    public String password;

    public LinkItAccount() {
        super(ProviderType.LinkIt);
    }
    
    @Override
    public String toString(){
        return "provider {" + provider + "}";
    }
}
