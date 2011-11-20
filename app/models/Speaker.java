package models;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

/**
 * A Mix-IT speaker, lecturing some sessions
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Speaker extends Member {

    @ManyToMany(mappedBy="speakers")
    Set<Talk> sessions = new HashSet<Talk>();
    
    /**
     * Authorized roles for a speaker
     */
    private static final Set<String> ROLES = new HashSet<String>();
    static {
        ROLES.add(Role.ADMIN_SESSION);
    }
    
    public Speaker(String login, Account account) {
        super(login, account);
        // Can't call addBadge() on a transient instance (and don't want to trigger activity)
        this.badges.add(Badge.Speaker);
    }

    @Override
    public boolean hasRole(String profile) {
        return ROLES.contains(profile);
    }
}
