package models.activity;

import models.Article;
import org.junit.*;

/**
 * Unit tests for {@link LookArticleActivity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LookArticleActivityTest extends AbstractActivityTest {

    @Test
    public void lookArticle() {
        
        Article a = Article.all().first();
        
        // No activity for the article
        assertEquals(0, Activity.count("article = ?", a));
        assertNull(Activity.find("article = ?", a).first());
        
        a.lookedBy(member);
        
        // One activity for the article
        assertEquals(1, Activity.count("article = ?", a));
        Activity act = Activity.find("article = ?", a).first();
        assertActivity(act);
        assertTrue(act instanceof LookArticleActivity);
        LookArticleActivity laa = (LookArticleActivity) act;
        assertEquals(a, laa.article);
        assertEquals(member, laa.member);
    }

    @Test
    public void lookarticleBySpeaker() {
        
        Article a = Article.all().first();
        
        // No activity for the article
        assertEquals(0, Activity.count("article = ?", a));
        assertNull(Activity.find("article = ?", a).first());
        
        // article looked by one of speakers
        a.lookedBy(a.author);
        
        // Still no activity for the article
        assertEquals(0, Activity.count("article = ?", a));
        assertNull(Activity.find("article = ?", a).first());
    }

    @Test
    public void lookarticleByNull() {
        
        Article a = Article.all().first();
        
        // No activity for the article
        assertEquals(0, Activity.count("article = ?", a));
        assertNull(Activity.find("article = ?", a).first());
        
        // A not connected user looked at the article
        a.lookedBy(null);
        
        // Still no activity for the article
        assertEquals(0, Activity.count("article = ?", a));
        assertNull(Activity.find("article = ?", a).first());
    }
}
