package models;

import org.apache.commons.lang.StringUtils;
import org.junit.*;

/**
 * Unit tests for {@link Article} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class ArticleTest extends BaseDataUnitTest {
    
    @Test
    public void saveWithBigContent() {
        final Member author = Member.all().first();
        Article article = new Article(author);
        String content = StringUtils.leftPad("testwith4000char", 4000+3000, "a");
        article.content = content;
        assertTrue(article.content.length()>4000);
        article.save();
    }
    
    @Test
    public void recents() {
        assertNotNull(Article.recents(1, 10));
    }
    
    private Member createMember(String login) {
        return new Member(login).save();
    }
    
    @Test public void lookBy() {
        final Article article = Article.all().first();
        final Member author = article.author;
        final Member toto = createMember("toto");
        final long nbLooks = article.getNbLooks();
        
        // If an author looks at his article, it is not counted
        article.lookedBy(author);
        assertEquals(nbLooks, article.getNbLooks());

        article.lookedBy(toto);
        assertEquals(nbLooks+1, article.getNbLooks());
        article.lookedBy(null);
        assertEquals(nbLooks+2, article.getNbLooks());
    }
}
