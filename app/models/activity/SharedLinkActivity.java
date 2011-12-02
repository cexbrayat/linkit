package models.activity;

import helpers.badge.BadgeComputationContext;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.ProviderType;
import models.SharedLink;
import play.i18n.Messages;

/**
 * A link sharing activity : someone ({@link Activity#member} shared a new link ({@link SharedLinkActivity#link}).
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class SharedLinkActivity extends Activity {

    @ManyToOne
    public SharedLink link;

    public SharedLinkActivity(SharedLink link) {
        super(ProviderType.LinkIt);
        this.member = link.member;
        this.link = link;
        // Useless badge computation
        this.badgeComputationDone = true;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        // Nothing
    }

    @Override
    public String getMessage(String lang) {
        return Messages.get(getMessageKey(), member, link);
    }

    @Override
    public String getUrl() {
        return link.URL;
    }
}
