package models;

import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import java.util.List;
import javax.persistence.ManyToOne;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Entity
public class Vote extends Model {

    @Required
    @ManyToOne
    public LightningTalk session;
    @Required
    @ManyToOne
    public Member member;

    /**
     * true if the vote is positive
     */
    public boolean value;

    public Vote(LightningTalk session, Member member, boolean value) {
        this.session = session;
        this.member = member;
        this.value = value;
    }

    public static List<Vote> findVotesBySession(LightningTalk session) {
        return Vote.find("select v from Vote where session = ? and value is true", session).fetch();
    }

    public static long findNumberOfVotesBySession(LightningTalk session) {
        return Vote.count("session = ? and value is true", session);
    }

    public static List<Vote> findVotesByMember(Member member) {
        return Vote.find("select v from Vote where member = ?", member).fetch();
    }

    public static Vote findVote(LightningTalk session, Member member) {
        return Vote.find("select v from Vote v where v.member = :member and v.session = :session").bind("member", member).bind("session", session).first();
    }
}
