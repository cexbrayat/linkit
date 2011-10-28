package models;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Suggestion of new member to be linked
 * @author agnes007 <agnes.crepet@gmail.com>
 */
public class Suggestion {

    /**
     * Find all Members Interested in AT LEAST ONE interest
     * @param interests
     * @return 
     */
    public static List<Member> findMembersInterestedInOneOf(Collection<Interest> interests) {
        return Member.find(
                "select distinct m from Member m join m.interests as i "
                + "where i in (:interests) group by m").bind("interests", interests).fetch();
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
     * Suggest all Members with the same interests
     * Currently : algorithm is base on the findMembersInterestedInOneOf method!
     * The suggested Members must only have AT LEAST ONE common interest with the member
     * These members must NOT contain the member!
     * These members must be different of the links (members he follows)
     * @param member
     * @return List of suggested member
     */
    // FIXME Suggested member should be ordered by pertinence, i.e. sharing most interests to less.
    public static Set<Member> suggestedMembersFor(Member member) {
        Set<Member> suggestions = Collections.emptySet();
        if (!member.interests.isEmpty()) {
            List<Member> allSuggestedMembers = findMembersInterestedInOneOf(member.interests);
            allSuggestedMembers.remove(member);
            Iterable suggestedMembers =
                    Iterables.filter(allSuggestedMembers, 
                        Predicates.not(Predicates.in(member.links))
                    );
            suggestions = Sets.newHashSet(suggestedMembers);
        }
        return suggestions;
    }
    
}
