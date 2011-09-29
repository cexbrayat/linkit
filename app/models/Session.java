package models;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
    public String title;
    @Required
    public String summary;
    @Required @Enumerated(EnumType.STRING)
    public Track track;
    
    /** Markdown enabled */
    @Lob @Required
    public String description;
    
    @ManyToMany
    public Set<Speaker> speakers;
    
    public void addSpeaker(Speaker speaker) {
        if (speaker != null) {
            speakers.add(speaker);
            speaker.sessions.add(this);
        }
    }
}
