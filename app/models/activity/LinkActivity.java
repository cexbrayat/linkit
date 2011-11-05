package models.activity;

import java.util.EnumSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.Badge;
import models.Member;
import models.ProviderType;
import play.data.validation.Required;
import play.i18n.Messages;
import play.mvc.Router;

/**
 * A link activity : someone ({@link Activity#member} starts to follow someone else ({@link LinkActivity#linked}
 * @author Agnes <agnes.crepet@gmail.com>
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LinkActivity extends Activity {

    @Required
    @ManyToOne
    public Member linked;

    public LinkActivity(Member member, Member linked) {
        super(ProviderType.LinkIt);
        this.member = member;
        this.linked = linked;
    }

    @Override
    public String getMessage(String lang) {
        return Messages.get(getMessageKey(), member, linked);
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Profile.show")
                .add("login", member.login)
                .addRef("linkTo"+linked.login)
                .url;
    }

    @Override
    public Set<Badge> getPotentialTriggeredBadges() {
        return EnumSet.of(Badge.StaffFriend, Badge.SpeakerFriend, Badge.Linkator1, Badge.Linkator5, Badge.Linkedator1, Badge.Linkedator5);
    }
}
