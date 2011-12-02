package models;

import javax.persistence.Entity;
import play.data.validation.Required;

/**
 * A Mix-IT sponsor, giving a fucking load of money to buy better sandwiches than the ones given at WSN Paris.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Sponsor extends Member {

    @Required
    public String logoUrl;
        
    public Sponsor(String login) {
        super(login);
        // Can't call addBadge() on a transient instance (and don't want to trigger activity)
        this.badges.add(Badge.Sponsor);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(firstname);
        if (lastname != null) {
           str.append(' ').append(lastname);
        }
        return str.toString();
    }   
}
