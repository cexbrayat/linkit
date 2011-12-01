package models;

import models.activity.CommentSessionActivity;
import models.activity.LookSessionActivity;
import models.activity.UpdateSessionActivity;
import org.apache.commons.lang.StringUtils;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.*;

/**
 * A talk session
 * @author Sryl <cyril.lacote@gmail.com>
 * @author Agnes <agnes.crepet@gmail.com>
 */
@Entity
public class Session extends Model implements Lookable {

    @Required
    @MaxSize(50)
    public String title;

    @Required
    @MaxSize(140)
    public String summary;

    @Required
    @Temporal(TemporalType.TIMESTAMP)
    public Date addedAt = new Date();

    @Required
    @Enumerated(EnumType.STRING)
    public Track track;
    /** Markdown enabled */
    @Lob
    @Required
    public String description;

    @ManyToMany
    @MinSize(1)
    public Set<Speaker> speakers = new HashSet<Speaker>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Interest> interests = new TreeSet<Interest>();

    /** Eventual comments */
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @OrderBy("postedAt ASC")
    List<SessionComment> comments;
    
    /** Number of consultation */
    public long nbConsults;

    public final void addSpeaker(Speaker speaker) {
        if (speaker != null) {
            speakers.add(speaker);
            speaker.sessions.add(this);
        }
    }

    public Session updateSpeakers(Collection<Speaker> speakers) {
        this.speakers.clear();
        for (Speaker speaker : speakers) {
            addSpeaker(speaker);
        }
        return this;
    }

    /**
     * Save comment! Best practices in add method?
     * @param comment 
     */
    public void addComment(SessionComment comment) {
        comment.session = this;
        comment.save();
        comments.add(comment);
        
        new CommentSessionActivity(comment.author, this, comment).save();
    }

    public static List<Session> findSessionsLinkedWith(String interest) {
        return Session.find(
                "select distinct s from Session s join s.interests as i where i.name = ?", interest).fetch();
    }
    
    public Session addInterest(String interest) {
        if (StringUtils.isNotBlank(interest)) {
            interests.add(Interest.findOrCreateByName(interest));
        }
        return this;
    }

    public Session addInterests(String... interests) {
        for (String interet : interests) {
            addInterest(interet);
        }
        return this;
    }

    public Session updateInterests(String... interests) {
        this.interests.clear();
        addInterests(interests);
        return this;
    }
    
    /**
     * Functional update of this session (having modified its data)
     */
    public void update() {
        save();
        new UpdateSessionActivity(this).save();
    }
    
    @Override
    public String toString() {
        return title;
    }

    public long getNbLooks() {
        return nbConsults;
    }

    public void lookedBy(Member member) {
        if (member == null || !speakers.contains(member)) {
            nbConsults++;
            save();
            if (member != null) {
                new LookSessionActivity(member, this).save();                
            }
        }
    }

    public static List<Session> recents(int page, int length) {
        return find("order by addedAt desc").fetch(page, length);
    }

}
