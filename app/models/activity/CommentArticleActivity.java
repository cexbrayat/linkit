package models.activity;

import com.google.common.collect.Sets;
import helpers.JavaExtensions;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.ArticleComment;
import models.Member;
import models.Article;
import models.Staff;
import play.data.validation.Required;
import play.mvc.Router;

import java.util.Collections;
import java.util.Set;

/**
 * A comment activity : someone ({@link Activity#member} commented on a Article ({@link Activity#Article}
 * @author Agnes <agnes.crepet@gmail.com>
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class CommentArticleActivity extends CommentActivity {

    @Required
    @ManyToOne
    public ArticleComment comment;

    public CommentArticleActivity(Member author, Article article, ArticleComment comment) {
        super(author);
        this.article = article;
        this.comment = comment;
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Articles.show")
                .add("articleId", article.id)
                .add("slugify", JavaExtensions.slugify(article.title))
                .addRef("comment"+comment.id)
                .url;
    }

    @Override
    public Set<Member> getNotifiableMembers() {
        return Collections.singleton(this.member);
    }
}
