package models.activity;

import models.Article;
import org.junit.*;

/**
 * Unit tests for {@link NewArticleActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class NewArticleActivityTest extends AbstractActivityTest {
    
    @Test
    public void validateArticle() {
        
        Article a = createArticle("test");
        
        // No activity for the article
        assertEquals(0, Activity.count("article = ?", a));
        assertNull(Activity.find("article = ?", a).first());
        
        a.validate();
        
        // One activity for the article
        assertEquals(1, Activity.count("article = ?", a));
        Activity act = Activity.find("article = ?", a).first();
        assertActivity(act);
        assertTrue(act instanceof NewArticleActivity);
        NewArticleActivity naa = (NewArticleActivity) act;
        assertEquals(a, naa.article);
    }
}
