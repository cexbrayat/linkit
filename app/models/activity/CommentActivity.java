package models.activity;

import helpers.badge.BadgeComputationContext;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import models.Badge;
import models.ConferenceEvent;
import models.Member;
import models.ProviderType;

/**
 * A comment activity : someone ({@link Activity#member} commented on something
 * @author Agnes <agnes.crepet@gmail.com>
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public abstract class CommentActivity extends Activity {

    public CommentActivity(Member author) {
        super(ProviderType.LinkIt, 2);
        this.member = author;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        member.computeBadges(EnumSet.of(Badge.Brave, Badge.Troller), context);
    }

    public static List<CommentActivity> between(Date start, Date end) {
        CriteriaBuilder builder = em().getCriteriaBuilder();
        CriteriaQuery<CommentActivity> cq = builder.createQuery(CommentActivity.class);
        Root<CommentActivity> activity = cq.from(CommentActivity.class);
        Predicate where = builder.conjunction();
        if (start != null) {
            Predicate after = builder.greaterThanOrEqualTo(activity.<Date>get(AT), start);
            where = builder.and(where, after);
        }
        if (end != null) {
            Predicate before = builder.lessThan(activity.<Date>get(AT), end);
            where = builder.and(where, before);
        }
        cq.where(where);
        cq.orderBy(builder.asc(activity.get(AT)));
        return em().createQuery(cq).getResultList();
    }

    public abstract Set<Member> getNotifiableMembers();
}
