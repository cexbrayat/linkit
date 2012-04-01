package models.auth;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import models.Member;
import models.ProviderType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * An authentication account for login on Link-IT
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@DiscriminatorValue("provider")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class AuthAccount extends Model {
    
    @Required
    @ManyToOne(optional = false)
    public Member member;

    // Authentication provider : linkit, twitter, google, ...
    @Required
    @Enumerated(EnumType.STRING)
    public ProviderType provider; 
    
    /** ID of last status retrieved */
    public String lastStatusId;
    
    public AuthAccount(ProviderType provider) {
        this.provider = provider;
    }
    
    public static AuthAccount find(ProviderType provider, String login) {
        return AuthAccount.find("from AuthAccount a where a.provider=?1 and a.member.login=?2", provider, login).first();
    }
    
    public static int deleteForMember(Member member) {
        return delete("from AuthAccount a where a.member=?", member);
    }
        
    /**
     * Initialize member profile from account data
     */
    public abstract void initMemberProfile();

    @Override
    public String toString(){
        return "AuthAccount provider {" + provider + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuthAccount other = (AuthAccount) obj;
        return new EqualsBuilder()
                .append(this.member, other.member)
                .append(this.provider, other.provider)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.member)
                .append(this.provider)
                .toHashCode();
    }
}
