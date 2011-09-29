package models;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;

/**
 * Don't mess with us.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Staff extends Member {
    
    /**
     * Authorized roles for a staff person
     */
    private static final Set<String> ROLES = new HashSet<String>();
    static {
        ROLES.add(Role.ADMIN_SESSION);
        ROLES.add(Role.ADMIN_SPEAKER);
        ROLES.add(Role.ADMIN_MEMBER);
        ROLES.add(Role.ADMIN_PLANNING);
    }

    public Staff(String login, Account account) {
        super(login, account);
        addBadge(Badge.Staff);
    }

    @Override
    public boolean hasRole(String profile) {
        return ROLES.contains(profile);
    }
    
}
