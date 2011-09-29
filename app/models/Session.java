package models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * A talk session
 * @author Sryl <cyril.lacote@gmail.com>
 * @author Agnes <agnes.crepet@gmail.com>
 */
@Entity
public class Session extends Model {

    @Required
    @MaxSize(50)
    public String title;
    @Required
    @MaxSize(140)
    public String summary;
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

    @Override
    public String toString() {
        return title;
    }
}
