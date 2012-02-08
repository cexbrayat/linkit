package models.activity;

import helpers.JavaExtensions;
import helpers.badge.BadgeComputationContext;
import javax.persistence.Entity;
import models.Article;
import models.ProviderType;
import play.mvc.Router;

/**
 * An "new article" activity : a talk ({@link Activity#article}) has been published
 * @author Sryl <cyril.lacote@gmail.com>
 */
@Entity
public class NewArticleActivity extends Activity {

    public NewArticleActivity(Article article) {
        super(ProviderType.LinkIt);
        this.article = article;
        this.badgeComputationDone = true;
    }

    @Override
    public String getUrl() {
        return Router
                .reverse("Articles.show")
                .add("articleId", this.article.id)
                .add("slugify", JavaExtensions.slugify(article.title))
                .url;
    }

    @Override
    protected void computedBadgesForConcernedMembers(BadgeComputationContext context) {
        // None
    }
}
