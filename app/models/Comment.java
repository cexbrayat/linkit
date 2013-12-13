package models;

import helpers.JavaExtensions;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.modules.search.Field;
import play.modules.search.Indexed;

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
@Indexed
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
    @Field
    public String content;
    
    @Column(name=POSTEDAT)
    @Required
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date postedAt;

    /* true if private comment. A private comment is visible only to admin members and owners of commented entity (like speakers of a Session). */
    @Column(name = "private")
    public boolean privatelyVisible;

    public Comment(Member author, String content) {
        this.author = author;
        this.setContent(content);
        this.postedAt = new Date();
    }

    public static int deleteForMember(Member member) {
        return delete("from Comment c where c.author = ?", member);
    }
    
    public static long countByMember(Member m) {
        return count("author=?", m);
    }
    
    public static List<Comment> recentsByMember(Member m, int size) {
        return find("from Comment c where c.author = ? order by c.postedAt desc", m).fetch(1, size);
    }

    public final void setContent(String content) {
        this.content = JavaExtensions.sanitizeHtml(content);
    }
    
    @Override
    public String toString() {
        return author + " le " + postedAt;
    }   
}
