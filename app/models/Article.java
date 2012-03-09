package models;

import java.util.ArrayList;
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
import models.activity.Activity;
import models.activity.CommentArticleActivity;
import models.activity.LookArticleActivity;
import models.activity.NewArticleActivity;
import org.apache.commons.lang.builder.CompareToBuilder;
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
public class Article extends Model implements Lookable, Comparable<Article> {

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

    /** True if Article validated : publicly visible */
    public boolean valid;
    
    /** Eventual comments */
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @OrderBy("postedAt ASC")
    List<ArticleComment> comments = new ArrayList<ArticleComment>();
    
    /** Number of consultation */
    public long nbConsults;

    public Article() {
        this.postedAt = new Date();
    }

    public Article(Member author, String title) {
        this();
        this.author = author;
        this.title = title;
    }

    public static List<Article> recents(int page, int length) {
        return find("valid=true order by postedAt desc").fetch(page, length);
    }

    public static List<Article> findAllInvalid() {
        return find("valid=false order by postedAt desc").fetch();
    }
    
    public Article findPrevious() {
        return find("valid = true and postedAt<? order by postedAt desc", this.postedAt).first();
    }
    
    public Article findFollowing() {
        return find("valid = true and postedAt>? order by postedAt asc", this.postedAt).first();
    }
    
    /**
     * Save comment! Best practices in add method?
     * @param comment 
     */
    public void addComment(ArticleComment comment) {
        comment.article = this;
        comment.save();
        comments.add(comment);
        
        if (valid) {
            new CommentArticleActivity(comment.author, this, comment).save();
        }
    }

    @Override
    public Article delete() {
        // Delete activities
        Activity.deleteForArticle(this);
        return super.delete();
    }
    
    public void validate() {
        this.valid = true;
        this.postedAt = new Date();
        this.save();
        new NewArticleActivity(this).save();
        
        // Publication des activités sur les hypothétiques commentaires existants
        for (ArticleComment comment : this.comments) {
            new CommentArticleActivity(comment.author, this, comment).save();
        }
    }
    
    public void unvalidate() {
        this.valid = false;
        Activity.deleteForArticle(this);
        this.save();
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

    public int compareTo(Article other) {
        return new CompareToBuilder().append(other.postedAt, this.postedAt).toComparison();
    }
}
