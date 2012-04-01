package models;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import play.db.jpa.GenericModel.JPAQuery;

/**
 * Suggestion of new member to be linked
 * @author agnes007 <agnes.crepet@gmail.com>
 */
public class Suggestion {

    /**
     * Find all sessions about AT LEAST ONE interest
     * @param interests
     * @return 
     */
    public static Collection<Session> findSessionsAbout(Collection<Interest> interests) {
        return Session.find(
                "select distinct s from Session s join s.interests as i "
                + "where s.valid=true and i in (:interests) group by s").bind("interests", interests).fetch();
    }

    /**
     * Find all Members Interested in ALL of the interests
     * @param interests
     * @return members interested
     */
    public static List<Member> findMembersInterestedInAllOf(Collection<Interest> interests) {
        return Member.find(
                "select distinct m from Member m join m.interests as i "
                + "where i in (:interests) group by m having count(i.id) = :size").bind("interests", interests).bind("size", interests.size()).fetch();
    }
    
    private static final Function<Object[], Member> MEMBER_SUGGESTIONS_FUNCTION = new Function<Object[], Member>() {
        public Member apply(Object[] tuple) {
            return (Member) tuple[0];
        }
    };
    /**
     * Suggest members sharing interests with given member. Results are ordered by the most commonly shared interests first.
     * @param member
     * @param limit max number of suggested members
     * @return List of suggested members, ordered by most interests shared first
     */
    // FIXME CLA rewrite with Criteria
    public static List<Member> suggestedMembersFor(Member member, int limit) {
        List<Member> suggestions = Collections.emptyList();
        if (!member.interests.isEmpty()) {
            StringBuilder query = new StringBuilder("select distinct suggested, count(i) as nbShared "
                    + "from Member suggested "
                    + "inner join suggested.interests as i "
                    + "where i in (:interests) "
                    + "and suggested <> :member ");
            if (!member.links.isEmpty()) {
                query.append("and suggested not in (:links) ");
            }
            query.append("group by suggested ")
                 .append("order by nbShared desc");
            
            JPAQuery jpaQuery = Member.find(query.toString())
                    .bind("member", member)
                    .bind("interests", member.interests);
            if (!member.links.isEmpty()) {
                    jpaQuery.bind("links", member.links);
            }
            List<Object[]> result = jpaQuery.fetch(limit);
            suggestions = Lists.transform(result, MEMBER_SUGGESTIONS_FUNCTION);
        }
        return suggestions;
    }
    
    private static final Function<Object[], Session> SESSION_SUGGESTIONS_FUNCTION = new Function<Object[], Session>() {
        public Session apply(Object[] tuple) {
            return (Session) tuple[0];
        }
    };
     /**
     * Suggest sessions sharing interests of given member
     * @param member
     * @param limit max number of suggested sessions
     * @return List of suggested sessions, ordered by most interests shared first
     */
    // TODO Don't suggest sessions already selected by member, when planning available
    // FIXME CLA rewrite with Criteria
    public static List<Session> suggestedSessionsFor(Member member, int limit) {
        List<Session> suggestions = Collections.emptyList();
        if (!member.interests.isEmpty()) {
            List<Object[]> result = Session.find("select distinct suggested, count(i) as nbShared "
                    + "from Session suggested "
                    + "inner join suggested.interests as i "
                    + "where suggested.valid=true "
                    + "and i in (:interests) "
                    + "group by suggested "
                    + "order by nbShared desc")
                    .bind("interests", member.interests)
                    .fetch(limit);
            suggestions = Lists.transform(result, SESSION_SUGGESTIONS_FUNCTION);
        }
        return suggestions;
    }
    
    public static Set<Badge> missingBadgesFor(Member member) {
        Set<Badge> badges = EnumSet.copyOf(Badge.EarnableBadges);
        if (!member.badges.isEmpty()) {
            badges.removeAll(member.badges);
        }
        return badges;
    }
}
