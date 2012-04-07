package models;

import java.util.List;
import models.activity.NewVoteActivity;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Entity
public class Vote extends Model {

    @Required
    @ManyToOne
    public Session session;
    
    @Required
    @ManyToOne
    public Member member;

    /**
     * true if the vote is positive
     */
    public boolean value;

    public Vote(Session session, Member member, boolean value) {
        this.session = session;
        this.member = member;
        this.value = value;
    }

    public static long findNumberOfVotesBySession(Session session) {
        return Vote.count("session = ? and value is true", session);
    }

    public static List<Member> findVotersBySession(Session session) {
        return find("select v.member from Vote v where v.session = ? and v.value is true", session).fetch();
    }

    public static long countVotesByMember(Member member) {
        return find("select count(v) from Vote v where v.member = :member").bind("member", member).first();
    }

    public static long deleteForMember(Member member) {
        return delete("delete Vote v where v.member = ?1", member);
    }

    public static long deleteForSession(Session session) {
        return delete("delete Vote v where v.session = ?1", session);
    }

    public static Vote findVote(Session session, Member member) {
        return Vote.find("select v from Vote v where v.member = :member and v.session = :session").bind("member", member).bind("session", session).first();
    }

    @Override
    public Vote save() {
        Vote v = super.save();
        if (value) {
            new NewVoteActivity(member, session).save();
        }
        return v;
    }
}
