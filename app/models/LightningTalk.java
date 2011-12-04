package models;

import play.Logger;

import javax.persistence.*;
import java.util.List;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Entity
public class LightningTalk extends Session {

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Vote> votes;

    public boolean hasVoteFrom(String username) {
        Member member = Member.findByLogin(username);
        if (member != null) {
            Vote vote = Vote.findVote(this, member);
            if (vote != null) {
                Logger.info(this.id + " - vote value: " + vote.value);
                return vote.value;
            }
        }
        return false;
    }

    public long getNumberOfVotes() {
        long numberOfVotesBySession = Vote.findNumberOfVotesBySession(this);
        return numberOfVotesBySession;
    }

    @Override
    public String toString() {
        return title;
    }
}
