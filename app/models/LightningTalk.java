package models;

import play.Logger;

import javax.persistence.*;
import java.util.List;
import play.modules.search.Indexed;
import play.mvc.Router;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Entity
@Indexed
public class LightningTalk extends Session {

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Vote> votes;

    public LightningTalk() {
        super();
        // A Lightning Talk is always validated
        valid = true;
    }

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
        return Vote.findNumberOfVotesBySession(this);
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public String getShowUrl() {
        return Router
                .reverse("LightningTalks.show")
                .add("sessionId", this.id)
                .url;
    }
}
