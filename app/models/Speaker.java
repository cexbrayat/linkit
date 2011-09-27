package models;

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
    Set<Session> sessions;
    
    public Speaker(String login, Account account) {
        super(login, account);
        addBadge(Badge.Speaker);
    }
}
