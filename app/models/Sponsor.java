package models;

import javax.persistence.Entity;

/**
 * A Mix-IT sponsor, giving a fucking load of money to buy better sandwiches than the ones given at WSN Paris.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Sponsor extends Member {

    public Sponsor(String login) {
        super(login);
        // Can't call addBadge() on a transient instance (and don't want to trigger activity)
        this.badges.add(Badge.Sponsor);
    }
}
