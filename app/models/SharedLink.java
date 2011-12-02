package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.activity.SharedLinkActivity;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.JPABase;
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
    @URL
    public String URL;

    @ManyToOne(optional = false)
    public Member member;

    public SharedLink(String name, String URL) {
        this.name = name;
        this.URL = URL;
    }

    @Override
    public SharedLink delete() {
        // Deleting corresponding activity
        SharedLinkActivity.delete("link = ?", this);
        return super.delete();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SharedLink other = (SharedLink) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return new EqualsBuilder()
                .append(this.name, other.name)
                .append(this.URL, other.URL)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.name)
                .append(this.URL)
                .toHashCode();
    }
}
