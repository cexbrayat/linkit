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
}
