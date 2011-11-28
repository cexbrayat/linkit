package models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 * A generic comment entity
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(
        appliesTo="Comment",
        indexes={
            @Index(name="Comment_Author_IDX", columnNames={Comment.AUTHOR_FK, Comment.POSTEDAT}),
            @Index(name="SessionComment_IDX", columnNames={SessionComment.SESSION_FK, Comment.POSTEDAT}),
            @Index(name="ArticleComment_IDX", columnNames={ArticleComment.ARTICLE_FK, Comment.POSTEDAT})
        }
)
public abstract class Comment extends Model {

    static final String AUTHOR_FK = "author_id";
    static final String POSTEDAT = "postedAt";

    @Required
    @ManyToOne
    @JoinColumn(name = SessionComment.AUTHOR_FK)
    public Member author;
    
    /** Markdown enabled */
    @Lob
    @Required
    public String content;
    
    @Column(name=POSTEDAT)
    @Required
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date postedAt;

    public Comment(Member author, String content) {
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }

    public static int deleteForMember(Member member) {
        return delete("from Comment c where c.author = ?", member);
    }
    
    public static long countByMember(Member m) {
        return count("author=?", m);
    }

    @Override
    public String toString() {
        return author + " le " + postedAt;
    }   
}
