package models;

import com.google.gson.JsonElement;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.activity.StatusActivity;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import play.Logger;
import play.Play;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.WS;

/**
 * An account on an social network
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@DiscriminatorValue("provider")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class Account extends Model implements Comparable<Account> {
    
    @Required
    @ManyToOne(optional = false)
    public Member member;

    // Authentication provider : linkit, twitter, google, ...
    @Required
    @Enumerated(EnumType.STRING)
    public ProviderType provider; 
    
    /** ID of last status retrieved */
    public String lastStatusId;
    
    /** Timestamp of last feed fetched */
    @Temporal(TemporalType.TIMESTAMP)
    public Date lastFetched;
    
    /** Timeout for WS fetching. Short because we can do it again next time. */
    protected static final String FETCH_TIMEOUT = Play.configuration.getProperty("linkit.timeline.fetch.timeout");
    
    public Account(ProviderType provider) {
        this.provider = provider;
    }
    
    /**
     * Fetch recent activities from provider for the given member's account
     * @param account member's account
     * @return activities last activities retrieved
     */
    public abstract List<StatusActivity> fetchActivities();
    
    /**
     * Enhance given status activities with Link-IT features (ex : link to Link-IT profile, ...)
     * @param activities status activities to enhance
     */
    public abstract void enhance(Collection<StatusActivity> activities);
    
    /**
     * @return URL of member's profile on this social network account
     */
    public abstract String url();
    
    /**
     * Fetch JSON data from given WS URL
     * @param url url to fetch JSON data from
     * @return JSON dat fetched, null if error or timeout.
     */
    protected JsonElement fetchJson(final String url) {
        JsonElement data = null;
        try {
            data = WS.url(url).timeout(FETCH_TIMEOUT).get().getJson();
            this.lastFetched = new Date();
        } catch (Exception e) {
            Logger.warn(e, "Error while fetching %s", url);
            data = null;
        }
        return data;
    }
    
    public static List<Long> findAllIds() {
        return find("select a.id from Account a").fetch();
    }

    @Override
    public String toString(){
        return "provider {" + provider + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Account other = (Account) obj;
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

    public int compareTo(Account other) {
        return this.provider.compareTo(other.provider);
    }
    
}
