package models;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import controllers.Application;
import controllers.Mails;
import java.util.*;
import javax.annotation.Nullable;
import javax.persistence.*;
import models.activity.Activity;
import models.activity.CommentSessionActivity;
import models.activity.LookSessionActivity;
import models.activity.UpdateSessionActivity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.modules.search.Field;

/**
 * A session
 * @author Sryl <cyril.lacote@gmail.com>
 * @author Agnes <agnes.crepet@gmail.com>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Session extends Model implements Lookable, Comparable<Session> {

    public static final String EVENT = "event";
    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String DESCRIPTION = "description";

    @Column(name = EVENT, nullable = false, updatable = false, length = 10)
    @Enumerated(EnumType.STRING)
    @Required
    public ConferenceEvent event;

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

    @Column
    @Lob
    @Field
    public String ideaForNow;

    @ManyToMany
    @Required
    public Set<Member> speakers = new HashSet<Member>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    public Set<Interest> interests = new TreeSet<Interest>();

    /** Eventual comments */
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @OrderBy("postedAt ASC")
    public List<SessionComment> comments = new ArrayList<SessionComment>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Vote> votes;

    /** Number of consultation */
    public long nbConsults;
    
    /** Is session validated (publicly visible) */
    public boolean valid;

    @Enumerated(EnumType.STRING)
    @Required   // But nullable : must give language when editing, but not always given on old sessions.
    public TalkLanguage lang;

    /* true if Staff has given feedback */
    public boolean feedback;

    protected Session() {
        this.event = ConferenceEvent.CURRENT;
    }

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
        return hasSpeaker(Member.findByLogin(username));
    }

    public boolean hasSpeaker(Member member) {
        if (member == null) return false;
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

    public List<SessionComment> getPublicComments() {
        List<SessionComment> result = new ArrayList<SessionComment>();

        for (SessionComment comment : comments) {
            if ( !comment.privatelyVisible ) {
                result.add(comment);
            }
        }
        return result;
    }

    public static <T extends Session> List<T> findValidatedLinkedWith(Interest interest) {
        return find("valid = true and ? in elements(interests)", interest).fetch();
    }

    public static <T extends Session> List<T> findBySpeaker(Member speaker, ConferenceEvent event) {
        return find("? in elements(speakers) and event = ?", speaker, event).fetch();
    }

    public static List<Session> findAllLinkedWith(Interest interest) {
        return find("? in elements(interests)", interest).fetch();
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

    public int compareTo(Session other) {
        return new CompareToBuilder().append(this.title, other.title).toComparison();
    }

    private static final Function<Session,ConferenceEvent> SESSION_TO_EVENT = new Function<Session, ConferenceEvent>() {
        @Override
        public ConferenceEvent apply(@Nullable Session session) {
            return session.event;
        }
    };

    public static Function<Session,ConferenceEvent> toEvent() {
        return SESSION_TO_EVENT;
    }

    public static List<Member> guestSpeakers(ConferenceEvent event) {
        return find("select distinct speaker " +
                "from Session s " +
                "inner join s.speakers speaker " +
                "inner join s.comments c " +
                "where s.guest = true " +
                "and s.event = ? " +
                "and s.valid = true", event).fetch();
    }

}
