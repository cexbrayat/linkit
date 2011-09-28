package models;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import play.db.jpa.Model;

/**
 * A talk session
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class Session extends Model {
    
    String title;
    String summary;
    
    /** Markdown enabled */
    String description;
    
    @ManyToMany
    Set<Speaker> speakers;
    
    public void addSpeaker(Speaker speaker) {
        if (speaker != null) {
            speakers.add(speaker);
            speaker.sessions.add(this);
        }
    }
}
