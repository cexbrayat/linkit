package models;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import play.modules.search.Indexed;

/**
 * Don't mess with us.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Indexed
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
        ROLES.add(Role.ADMIN_ARTICLE);
        ROLES.add(Role.ADMIN_INTEREST);
    }

    public Staff(String login) {
        super(login);
        // Can't call addBadge() on a transient instance (and don't want to trigger activity)
        this.badges.add(Badge.Staff);
    }

    @Override
    public void updateTicketingRegistered(String yurplanToken) {
        // Staff always have a ticket
        setTicketingRegistered(true);
    }

    @Override
    public boolean hasRole(String profile) {
        return ROLES.contains(profile);
    }

    @Override
    public void setTicketingRegistered(boolean ticketingRegistered) {
        // A staff member is always registered
        this.ticketingRegistered = true;
    }

    @Override
    public boolean canSeePrivateCommentsOf(Talk talk) {
        // Staff can see every comment
        return true;
    }
}
