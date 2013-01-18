package models;

import models.activity.Activity;
import models.activity.NewTalkActivity;
import play.data.validation.Required;
import play.modules.search.Indexed;
import play.mvc.Router;

import javax.persistence.*;
import java.util.List;
import models.activity.CommentSessionActivity;

/**
 * A talk session
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Indexed
public class Talk extends Session {

    @Enumerated(EnumType.STRING)
    public Track track;

    /** Bénéfices pour les participants */
    @Lob
    public String benefits;

    @Required
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public TalkFormat format = TalkFormat.Talk;

    public Integer maxAttendees;

    @Required
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public TalkLevel level = TalkLevel.Experienced;

    /** Markdown enabled */
    @Lob
    public String comment;

    public static List<Talk> findAllOn(ConferenceEvent event) {
        return find("event = ?", event).fetch();
    }

    public static List<Talk> findLinkedWith(Interest interest) {
        return find("valid=true and ? in elements(interests)", interest).fetch();
    }

    public static List<Talk> recents(int page, int length) {
        return find("valid=true order by addedAt desc").fetch(page, length);
    }
    
    public static long countSpeakers() {
        return find("select count(distinct s) from Talk t inner join t.speakers as s where t.valid=true and t.event = ?", ConferenceEvent.CURRENT).first();
    }

    public static List<Member> findAllSpeakers() {
        return findAllSpeakersOn(ConferenceEvent.CURRENT);
    }

    public static List<Member> findAllSpeakersOn(ConferenceEvent event) {
        return find("select distinct t.speakers from Talk t where t.valid=true and t.event = ?", event).fetch();
    }

    public static List<Talk> findAllValidatedOn(ConferenceEvent event) {
        return find("event = ? and valid=true", event).fetch();
    }

    public void validate() {
        this.valid = true;
        save();
        new NewTalkActivity(this).save();
        
        // Publication des activités sur les hypothétiques commentaires existants
        for (SessionComment comment : this.comments) {
            new CommentSessionActivity(comment.author, this, comment).save();
        }
    }
    
    public void unvalidate() {
        this.valid = false;
        Activity.deleteForSession(this);
        save();
    }

    @Override
    public void lookedBy(Member member) {
        if (valid) {
            super.lookedBy(member);
        }
        // Un talk non validé n'est pas consultable par le public : on ne compte pas les visites, ni ne génère d'activité
    }

    @Override
    public String getShowUrl() {
        return Router
                .reverse("Sessions.show")
                .add("sessionId", this.id)
                .add("slugify", this.title)
                .url;
    }
}
