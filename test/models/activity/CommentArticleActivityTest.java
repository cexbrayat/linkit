package models.activity;

import models.Article;
import models.ArticleComment;
import org.junit.*;

/**
 * Unit tests for {@link CommentArticleActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class CommentArticleActivityTest extends AbstractActivityTest {

    @Test
    public void addComment() {
        Article article = Article.all().first();
        
        // Non activity for the article
        assertEquals(0, Activity.count("article = ?", article));
        
        ArticleComment c = new ArticleComment(member, article, "Un commentaire");
        article.addComment(c);
        article.save();
        
        // One activity for the article
        assertEquals(1, Activity.count("article = ?", article));
        Activity a = Activity.find("article = ?", article).first();
        assertActivity(a);
        assertTrue(a instanceof CommentArticleActivity);
        CommentArticleActivity ca = (CommentArticleActivity) a;
        assertEquals(member, ca.member);
        assertEquals(article, ca.article);
        assertEquals(c, ca.comment);
    }
}
