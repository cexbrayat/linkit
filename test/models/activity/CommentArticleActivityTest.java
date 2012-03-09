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
    public void addCommentValid() {
        Article article = createArticle("test");
        article.valid = true;
        article.save();
        
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

    @Test
    public void addCommentNonValid() {
        Article article = createArticle("test");
        article.valid = false;
        article.save();
        
        // No activity for the article
        assertEquals(0, Activity.count("article = ?", article));
        
        ArticleComment c = new ArticleComment(member, article, "Un commentaire");
        article.addComment(c);
        article.save();
        
        // Still no activity for the article
        assertEquals(0, Activity.count("article = ?", article));
        
        article.validate();
        // 1 comment activity amongst others
        assertEquals(1, CommentArticleActivity.count("article = ?", article));
        Activity a = CommentArticleActivity.find("article = ?", article).first();
        assertActivity(a);
        assertTrue(a instanceof CommentArticleActivity);
        CommentArticleActivity ca = (CommentArticleActivity) a;
        assertEquals(member, ca.member);
        assertEquals(article, ca.article);
        assertEquals(c, ca.comment);
    }
}
