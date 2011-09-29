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

    //@ManyToMany(mappedBy="speakers")
    @ManyToMany
    Set<Session> sessions = new HashSet<Session>();
    
    /**
     * Authorized roles for a speaker
     */
    private static final Set<String> ROLES = new HashSet<String>();
    static {
        ROLES.add(Role.ADMIN_SESSION);
    }
    
    public Speaker(String login, Account account) {
        super(login, account);
        addBadge(Badge.Speaker);
    }

    @Override
    public boolean hasRole(String profile) {
        return ROLES.contains(profile);
    }
}
