package models;

import java.util.List;
import java.util.Map;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import play.Logger;
import play.db.jpa.Model;

/**
 * Member Interest
 * @author agnes007
 * @author Sryl
 */
@Entity
public class Interest extends Model implements Comparable<Interest> {

    public String name;

    protected Interest(String name) {
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
     * Find existing interest (no lazy creation)
     * @param name Interest named
     * @return Interest found, or null.
     */
    public static Interest findByName(String name) {
        return Interest.find("byName", name).first();
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

    public static List<Interest> findAllOrdered() { // Can't be named findAll() : DuplicateMemberException
        return find("order by name").fetch();
    }

    /**
     * Delete existing interest by name
     * @param name Interest name
     */
    public static void deleteByName(String name) {
        Interest interestToBeDeleted = Interest.findByName(name);
        List<Member> members = Member.findMembersInterestedIn(name);
        for (Member member : members) {
            member.interests.remove(interestToBeDeleted);
            member.save();
        }
        List<Session> sessions = Session.findSessionsLinkedWith(name);
        for (Session session : sessions) {
            session.interests.remove(interestToBeDeleted);
            session.save();
        }
        delete("name", name);
    }

    public static void deleteByName(String... interests) {
        for (String interet : interests) {
            deleteByName(interet);
        }
    }

    /**
     * Merge to another Interest
     * 1 - First : Retrieve the members and the sessions linked to this interest and relinked these elements to the survivor interest 
     * 2 - Then : Delete this interest
     * @param survivorInterest the survivor interest (the interest which we keep)
     */
    public void merge(Interest survivorInterest) {
        if (this.equals(survivorInterest)) {
            Logger.info("impossible de merger des interets identiques!");
            return;
        }
        List<Member> members = Member.findMembersInterestedIn(this.name);
        for (Member member : members) {
            member.interests.remove(this);
            member.interests.add(survivorInterest);
            member.save();
        }
        List<Session> sessions = Session.findSessionsLinkedWith(this.name);
        for (Session session : sessions) {
            session.interests.remove(this);
            session.interests.add(survivorInterest);
            session.save();
        }
        delete("name", this.name);

    }

    public int compareTo(Interest otherInterest) {
        return name.compareTo(otherInterest.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Interest other = (Interest) obj;
        return new EqualsBuilder().append(this.name, other.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.name).toHashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
