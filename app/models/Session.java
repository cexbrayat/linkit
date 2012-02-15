package models;

import controllers.Mails;
import models.activity.CommentSessionActivity;
import models.activity.LookSessionActivity;
import models.activity.UpdateSessionActivity;
import org.apache.commons.lang.StringUtils;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.*;
import models.activity.Activity;
import play.db.jpa.JPABase;
import play.modules.search.Field;

/**
 * A session
 * @author Sryl <cyril.lacote@gmail.com>
 * @author Agnes <agnes.crepet@gmail.com>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Session extends Model implements Lookable {

    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String DESCRIPTION = "description";
    
    @Column(name = TITLE)
    @Required
    @MaxSize(50)
    @Field
    public String title;

    @Column(name = SUMMARY)
    @Required
    @MaxSize(140)
    @Field
    public String summary;

    @Required
    @Temporal(TemporalType.TIMESTAMP)
    public Date addedAt = new Date();

    /** Markdown enabled */
    @Column(name = DESCRIPTION)
    @Lob
    @Required
    @Field
    public String description;

    @ManyToMany
    @Required
    public Set<Member> speakers = new HashSet<Member>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Interest> interests = new TreeSet<Interest>();

    /** Eventual comments */
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @OrderBy("postedAt ASC")
    public List<SessionComment> comments = new ArrayList<SessionComment>();
    
    /** Number of consultation */
    public long nbConsults;
    
    /** Is session validated (publicly visible) */
    public boolean valid;

    public final void addSpeaker(Member speaker) {
        if (speaker != null) {
            speakers.add(speaker);
            speaker.sessions.add(this);
        }
    }

    public final void removeSpeaker(Member speaker) {
        if (speaker != null) {
            speakers.remove(speaker);
            speaker.sessions.remove(this);
        }
    }

    public Session updateSpeakers(Collection<Member> speakers) {
        this.speakers.clear();
        for (Member speaker : speakers) {
            addSpeaker(speaker);
        }
        return this;
    }

    public boolean hasSpeaker(String username) {
        if (StringUtils.isBlank(username)) return false;
        Member member = Member.findByLogin(username);
        return speakers.contains(member);
    }

    /**
     * Save comment! Best practices in add method?
     * @param comment 
     */
    public void addComment(SessionComment comment) {
        comment.session = this;
        comment.save();
        comments.add(comment);

        // On ne déclenche une activité publique de mise à jour que si la session est valide (donc visible publiquement)
        if (valid) {
            new CommentSessionActivity(comment.author, this, comment).save();
        }
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
        // On ne déclenche une activité publique de mise à jour que si la session est valide (donc visible publiquement)
        if (valid) {
            new UpdateSessionActivity(this).save();
        }
        Mails.updateSession(this);
    }

    @Override
    public Session delete() {
        Activity.deleteForSession(this);
        Vote.deleteForSession(this);
        return super.delete();
    }

    @Override
    public String toString() {
        return title;
    }

    public long getNbLooks() {
        return nbConsults;
    }

    public void lookedBy(Member member) {
        if (valid) {
            if (member == null || !speakers.contains(member)) {
                nbConsults++;
                save();
                if (member != null) {
                    new LookSessionActivity(member, this).save();                
                }
            }
        }
    }
    
    /**
     * @return URL of display page for this session
     */
    public abstract String getShowUrl();
}
