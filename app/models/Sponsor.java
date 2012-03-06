package models;

import javax.persistence.Entity;
import org.apache.commons.lang.StringUtils;
import play.data.validation.Required;
import play.modules.search.Indexed;

/**
 * A Mix-IT sponsor, giving a fucking load of money to buy better sandwiches than the ones given at WSN Paris.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Indexed
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
        StringBuilder str = new StringBuilder();
        if (StringUtils.isNotBlank(firstname)) {
            str.append(firstname);
        }
        if (StringUtils.isNotBlank(lastname)) {
            if (str.length() > 0) str.append(' ');
            str.append(lastname);
        }
        return str.toString();
    }
}
