package models;

import java.util.List;
import java.util.Map;
import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * Member Interest
 * @author agnes007
 */
@Entity
public class Interest extends Model implements Comparable<Interest> {

    public String name;

    private Interest(String name) {
        this.name = name;
    }

    /**
     * Because want something like lazy interest creation
     * we will always get them using the findOrCreateByName(String name) factory method.
     * @param name
     * @return Interest
     */
    public static Interest findOrCreateByName(String name) {
        Interest interest = Interest.find("byName", name).first();
        if (interest == null) {
            interest = new Interest(name);
        }
        return interest;
    }

    /**
     * We use a handy Hibernate feature that allows to return a custom object from a JPA query
     * It will result a List containing for each interest a Map with two attributes:
     *     - name for the interest name
     *     - and pound for the interest count
     * @return 
     */
    public static List<Map> getCloud() {
        List<Map> result = Interest.find(
                "select new map(i.name as interest, count(m.id) as pound) "
                + "from Member m join m.interests as i group by i.name order by i.name").fetch();
        return result;
    }

    public int compareTo(Interest otherInterest) {
        return name.compareTo(otherInterest.name);
    }

    public String toString() {
        return name;
    }
}
