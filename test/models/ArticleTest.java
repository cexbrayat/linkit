package models;

import java.util.Date;
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
        Article article = createArticle("test", author);
        String content = StringUtils.leftPad("testwith4000char", 4000+3000, "a");
        article.content = content;
        assertTrue(article.content.length()>4000);
        article.save();
    }
    
    @Test
    public void recents() {
        assertNotNull(Article.recents(1, 10));
    }
    
    @Test
    public void findAllInvalid() {
        assertNotNull(Article.findAllInvalid());
    }
    
    @Test
    public void findFollowing() {
        final Member author = Member.all().first();
        final Article a1 = createArticle("titre1 valide", author);
        a1.validate();
        final Article a2 = createArticle("titre2 non valide", author);
        final Article a3 = createArticle("titre3 valide", author);
        a3.validate();
        final Article a4 = createArticle("titre4 non valide", author);
        final Article a5 = createArticle("titre5 valide", author);
        a5.validate();
        Article current = Article.findById(a1.id);
        assertSame(a3, current.findFollowing());
    }
    
    @Test
    public void findPrevious() {
        final Member author = Member.all().first();
        final Article a1 = createArticle("titre1 valide", author);
        a1.validate();
        final Article a2 = createArticle("titre2 non valide", author);
        final Article a3 = createArticle("titre3 valide", author);
        a3.validate();
        final Article a4 = createArticle("titre4 non valide", author);
        final Article a5 = createArticle("titre5 valide", author);
        a5.validate();
        Article current = Article.findById(a5.id);
        assertSame(a3, current.findPrevious());
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
    
    private Article createArticle(String title, Member author) {
        return new Article(author, title).save();
    }
    
    @Test public void validate() {
        final long nbPublicArticles = Article.count("valid=true");
        
        final Member author = Member.all().first();
        Article a = createArticle("test", author);
        assertEquals(nbPublicArticles, Article.count("valid=true"));
        final Date initialDate = a.postedAt;
        
        a.validate();
        assertEquals(nbPublicArticles+1, Article.count("valid=true"));
        assertTrue(a.postedAt.after(initialDate));
    }
    
    @Test public void unvalidate() {
        
        final Member author = Member.all().first();
        Article a = createArticle("test", author);
        a.validate();

        final long nbPublicArticles = Article.count("valid=true");
        
        a.unvalidate();
        assertEquals(nbPublicArticles-1, Article.count("valid=true"));
    }
    
    @Test public void delete() {
        
        final Member author = Member.all().first();
        Article a = createArticle("test", author);
        a.validate();
        a.addComment(new ArticleComment(author, a, "commentaire"));
        a.addComment(new ArticleComment(author, a, "autre commentaire"));
        a.save();

        final long nbPublicArticles = Article.count("valid=true");
        
        a.delete();
        assertEquals(nbPublicArticles-1, Article.count("valid=true"));
    }
}
