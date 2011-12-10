package models;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import models.activity.CommentArticleActivity;
import models.activity.LookArticleActivity;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.modules.search.Field;
import play.modules.search.Indexed;

/**
 * An article, i.e. a blog post
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Table(appliesTo = "Article",
indexes = {
    @Index(name = "Article_IDX", columnNames = {Article.POSTEDAT})
})
@Indexed
public class Article extends Model implements Lookable {

    public final static String TITLE = "title";
    public final static String HEADLINE = "headline";
    public final static String CONTENT = "content";
    public final static String POSTEDAT = "postedAt";    
    
    @Required
    @ManyToOne(optional = false)
    public Member author;

    @Required
    @Column(name = POSTEDAT)
    @Temporal(TemporalType.TIMESTAMP)
    public Date postedAt = new Date();
    
    @Column(name = TITLE)
    @Required
    @MaxSize(50)
    @Field
    public String title;

    /** Markdown enabled */
    @Column(name = HEADLINE)
    @Required
    @MaxSize(500)
    @Field
    public String headline;

    /** Markdown enabled */
    @Column(name = CONTENT)
    @Lob
    @Required
    @Field
    public String content;

    /** Eventual comments */
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @OrderBy("postedAt ASC")
    List<ArticleComment> comments;
    
    /** Number of consultation */
    public long nbConsults;

    public Article(Member author) {
        this.author = author;
        this.postedAt = new Date();
    }

    public static List<Article> recents(int page, int length) {
        return find("order by postedAt desc").fetch(page, length);
    }
    
    public Article findPrevious() {
        return find("postedAt<? order by postedAt desc", this.postedAt).first();
    }
    
    public Article findFollowing() {
        return find("postedAt>? order by postedAt desc", this.postedAt).first();
    }
    
    /**
     * Save comment! Best practices in add method?
     * @param comment 
     */
    public void addComment(ArticleComment comment) {
        comment.article = this;
        comment.save();
        comments.add(comment);
        
        new CommentArticleActivity(comment.author, this, comment).save();
    }
    
    @Override
    public String toString() {
        return title;
    }

    public long getNbLooks() {
        return nbConsults;
    }

    public void lookedBy(Member member) {
        if (member == null || !member.equals(author)) {
            nbConsults++;
            save();
            if (member != null) {
                new LookArticleActivity(member, this).save();                
            }
        }
    }
}
