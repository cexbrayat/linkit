package models;

import org.apache.commons.lang.StringUtils;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.modules.search.Indexed;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * A Mix-IT sponsor, giving a fucking load of money to buy better sandwiches than the ones given at WSN Paris.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Indexed
public class Sponsor extends Member {

    public enum Level{
      // Order is used at display-time.
      GOLD, SILVER, BRONZE
    }

    @Required
    public String logoUrl;

    @Enumerated(EnumType.STRING)
    public Level level;

	@ElementCollection @Enumerated(EnumType.STRING)
    @MinSize(1)
    public Set<ConferenceEvent> events = EnumSet.noneOf(ConferenceEvent.class);

	public Sponsor(String login) {
        super(login);
        // Can't call addBadge() on a transient instance (and don't want to trigger activity)
        this.badges.add(Badge.Sponsor);
    }

    public static long countOn(ConferenceEvent event) {
        return find("select count(distinct s) from Sponsor s join s.events e where e = ?", event).<Long>first();
    }

    /**
     * List sponsors for given event
     */
    public static List<Sponsor> findOn(ConferenceEvent event){
        return Sponsor.find("select distinct s from Sponsor s join s.events e where e = ?", event).fetch();
    }

    /**
     * List sponsors by level for given event
     */
    public static List<Sponsor> findByEventAndLevel(ConferenceEvent event, Level level){
        return Sponsor.find("select distinct s from Sponsor s join s.events e where s.level = ? and e = ?", level, event).fetch();
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
