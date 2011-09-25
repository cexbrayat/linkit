package models;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * An account on an authentication provider
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@DiscriminatorValue("provider")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Account extends Model {
    
    @Required
    @OneToOne(optional = false)
    public Member member;

    // Authentication provider : linkit, twitter, google, ...
    @Required
    @Enumerated(EnumType.STRING)
    public ProviderType provider; 
    
    public Account(ProviderType provider) {
        this.provider = provider;
    }
    
    /**
     * Initialize member profile from account data
     */
    public abstract void initMemberProfile();
    
    public static Account find(ProviderType provider, String login) {
        return Account.find("from Account a where a.provider=?1 and a.member.login=?2", provider, login).first();
    }

    @Override
    public String toString(){
        return "provider {" + provider + "}";
    }
}
