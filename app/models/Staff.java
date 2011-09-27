package models;

import javax.persistence.Entity;

/**
 * Don't mess with us.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Staff extends Member {

    public Staff(String login, Account account) {
        super(login, account);
        addBadge(Badge.Staff);
    }
}
