package helpers.oauth;

import helpers.oauth.Twitter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import models.Member;
import models.ProviderType;
import models.activity.StatusActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link Twitter}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TwitterTest extends UnitTest {

    private Twitter twitter = new Twitter();
    
    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }
    
    protected StatusActivity buildTweet(Member author, String content) {
        return new StatusActivity(author, new Date(), ProviderType.Twitter, content, null, null);
    }
    
    @Test
    public void enhance() {
        Member author = Member.findByLogin("ced");
        Member rguy = Member.findByLogin("rguy");
        final String content1 = "Hey @" + rguy.twitterName + " did you tweet about @toto or not?";
        final StatusActivity tweet1 = buildTweet(author, content1);
        final String content2 = "no mention";
        final StatusActivity tweet2 = buildTweet(author, content2);
        
        
        List<StatusActivity> activities = Arrays.asList(tweet1, tweet2);
        // Tested method
        twitter.enhance(activities);
        
        // List preserved
        assertEquals(2, activities.size());
        assertSame(tweet1, activities.get(0));
        assertSame(tweet2, activities.get(1));
        // Content enhanced on tweet1
        assertFalse(content1.equals(tweet1.content));
        assertTrue(tweet1.content.contains("<a href=\"/profile/show?login="+rguy.login+"\">@"+rguy.twitterName+"</a>"));
        assertTrue(tweet1.content.contains("<a href=\"http://www.twitter.com/toto\">@toto</a>"));
        // Content same on tweet2
        assertEquals(content2, tweet2.content);
    }
}
