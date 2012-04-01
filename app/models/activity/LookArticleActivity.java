package models.activity;

import helpers.JavaExtensions;
import javax.persistence.Entity;
import models.Article;
import models.Member;
import play.mvc.Router;

/**
 * A consultation of an article : someone ({@link Activity#member}) looked at an article ({@link Activity#article}).
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class LookArticleActivity extends LookActivity {

    public LookArticleActivity(Member member, Article article) {
        super(member);
        this.article = article;
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Articles.show")
                .add("articleId", article.id)
                .add("slugify", JavaExtensions.slugify(article.title))
                .url;
    }
}
