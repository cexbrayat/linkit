package models.activity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.ArticleComment;
import models.Member;
import models.Article;
import play.data.validation.Required;
import play.i18n.Messages;
import play.mvc.Router;
import play.mvc.Scope;

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
    public String getMessage(Scope.Session s) {
        return Messages.get(getMessageKey(), member, article, comment);
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Articles.show")
                .add("articleId", article.id)
                .addRef("comment"+comment.id)
                .url;
    }
}
