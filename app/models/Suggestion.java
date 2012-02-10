package models;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
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
    public static List<Session> findSessionsAbout(Collection<Interest> interests) {
        return Session.find(
                "select distinct s from Session s join s.interests as i "
                + "where i in (:interests) group by s").bind("interests", interests).fetch();
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
    
     /**
     * Suggest members sharing interests with given member. Results are ordered by the most commonly shared interests first.
     * @param member
     * @param limit max number of suggested members
     * @return List of suggested member
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
            suggestions = Lists.transform(result, new Function<Object[], Member>() {
                public Member apply(Object[] tuple) {
                    return (Member) tuple[0];
                }
            });
        }
        return suggestions;
    }
    
     /**
     * Suggest all sessions sharing interests of given member
     * @param member
     * @return Set of suggested sessions
     */
    public static Set<Session> suggestedSessionsFor(Member member) {
        Set<Session> sessions = Sets.newHashSet();
        if (!member.interests.isEmpty()) {
            List<Session> allSuggestedSessions = findSessionsAbout(member.interests);
            // TODO Don't suggest a session where member has already plan to go.
            sessions.addAll(allSuggestedSessions);
        }
        return sessions;
    }
    
    public static Set<Badge> missingBadgesFor(Member member) {
        // EnumSet.copyOf can't be used on empty collection
        Set<Badge> badges = Badge.EarnableBadges;
        if (!member.badges.isEmpty()) {
            badges.removeAll(member.badges);
        }
        return badges;
    }
}
