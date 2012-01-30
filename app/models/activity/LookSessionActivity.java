package models.activity;

import javax.persistence.Entity;
import models.Member;
import models.Session;

/**
 * A consultation of session : someone ({@link Activity#member}) looked at a session ({@link Activity#session}).
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LookSessionActivity extends LookActivity {

    public LookSessionActivity(Member member, Session session) {
        super(member);
        this.session = session;
    }

    @Override
    public String getUrl() {
        return session.getShowUrl();
    }
}
