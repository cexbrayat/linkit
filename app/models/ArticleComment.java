package models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.collect.Sets;
import play.data.validation.Required;
import play.modules.search.Indexed;

import java.util.Collections;
import java.util.Set;

/**
 * A comment on e session talk.
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
@Indexed
public class ArticleComment extends Comment {

    static final String ARTICLE_FK = "article_id";
    
    @Required
    @ManyToOne
    @JoinColumn(name=ARTICLE_FK)
    public Article article;

    public ArticleComment(Member author, Article article, String content) {
        super(author, content);
        this.article = article;
    }

    @Override
    public Set<Member> getNotifiableMembers() {
        // Don't notify author of comment
        if (!author.equals(article.author)) {
            return Collections.singleton(article.author);
        } else {
            return Collections.emptySet();
        }
    }
}
