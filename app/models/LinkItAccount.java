package models;

import javax.persistence.Entity;

/**
 * An account on Link-IT (basic login/password local authentication)
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LinkItAccount extends Account {

    public String password;

    public LinkItAccount(String password) {
        super(ProviderType.LinkIt);
        this.password = password;
    }
    
    @Override
    public String toString(){
        return "provider {" + provider + "}";
    }

    @Override
    public void initMemberProfile() {
        // Nothing to initialize;
    }
}
