package models;

import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Julien Ripault <tluapir@gmail.com>
 */
@Entity
public class LightningTalk extends Model {
    @Required
    @MaxSize(50)
    public String title;
    @Required
    @MaxSize(140)
    public String summary;

    /**
     * Markdown enabled
     */
    @Lob
    @Required
    public String description;

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Interest> interests = new TreeSet<Interest>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Vote> votes;

    @ManyToOne
    public Member speaker;

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

    public boolean hasSpeaker(String username) {
        Member member = Member.findByLogin(username);
        return speaker.equals(member);
    }

    public long getNumberOfVotes() {
        long numberOfVotesBySession = Vote.findNumberOfVotesBySession(this);
        return numberOfVotesBySession;
    }

    public void addInterest(String interest) {
        if (StringUtils.isNotBlank(interest)) {
            interests.add(Interest.findOrCreateByName(interest));
        }
    }

    public void addInterests(String... interests) {
        for (String interest : interests) {
            addInterest(interest);
        }
    }

    public void updateInterests(String... interests) {
        this.interests.clear();
        addInterests(interests);
    }

    @Override
    public String toString() {
        return title;
    }
}
