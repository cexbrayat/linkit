package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * An URL shared by a member on his profile
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class SharedLink extends Model {

    public int ordernum;    // "order" is a SQL reserved keyword

    @Required
    @MaxSize(25)
    public String name;

    @Required
    public String URL;

    @ManyToOne(optional = false)
    public Member member;

    public SharedLink(String name, String URL) {
        this.name = name;
        this.URL = URL;
    }
    
}
