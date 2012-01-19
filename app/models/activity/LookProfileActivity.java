package models.activity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.Member;
import play.i18n.Messages;
import play.mvc.Router;
import play.mvc.Scope;

/**
 * A consultation of profile activity : someone ({@link Activity#member} looked at someone else's ({@link LookProfileActivity#other} profile.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LookProfileActivity extends LookActivity {

    @ManyToOne
    public Member other;

    public LookProfileActivity(Member member, Member consulted) {
        super(member);
        this.other = consulted;
    }

    @Override
    public String getMessage(Scope.Session session) {
        return Messages.get(getMessageKey(), member, other);
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Profile.show")
                .add("login", other.login)
                .url;
    }
}
