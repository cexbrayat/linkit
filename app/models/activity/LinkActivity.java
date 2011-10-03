package models.activity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.Member;
import play.data.validation.Required;

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
        super();
        this.member = member;
        this.linked = linked;
    }

    @Override
    public String getMessage(String lang) {
        // TODO;
        return "Link " + member + " -> " + linked;
    }
}
