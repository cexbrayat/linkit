package models;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import models.activity.StatusActivity;
import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link TwitterAccount}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TwitterAccountTest extends UnitTest {
    
    protected static StatusActivity buildTweet(Member author, String content) {
        return new StatusActivity(author, new Date(), ProviderType.Twitter, content, null, null);
    }
    
    protected static TwitterAccount createMemberAndAccount(final String login, final String twitterName) {
        TwitterAccount ta = new TwitterAccount(twitterName);
        new Member(login, ta).save();
        return ta;
    }
    
    @Test
    public void enhance() {
        TwitterAccount auteurAccount = createMemberAndAccount("auteur", "leplusgrandauteur");
        TwitterAccount mentionnedAccount = createMemberAndAccount("rguy","rguy");
        final String content1 = "Hey @" + mentionnedAccount.screenName + " did you tweet about @toto or not?";
        final StatusActivity tweet1 = buildTweet(auteurAccount.member, content1);
        final String content2 = "no mention";
        final StatusActivity tweet2 = buildTweet(auteurAccount.member, content2);
        
        List<StatusActivity> activities = Arrays.asList(tweet1, tweet2);
        // Tested method
        auteurAccount.enhance(activities);

        // List preserved
        assertEquals(2, activities.size());
        assertSame(tweet1, activities.get(0));
        assertSame(tweet2, activities.get(1));
        // Content enhanced on tweet1
        assertFalse(content1.equals(tweet1.content));
        assertTrue(tweet1.content.contains("<a href=\"/profile/show?login="+mentionnedAccount.member.login+"\">@"+mentionnedAccount.screenName+"</a>"));
        assertTrue(tweet1.content.contains("<a href=\"http://www.twitter.com/toto\">@toto</a>"));
        // Content same on tweet2
        assertEquals(content2, tweet2.content);
    }
    
    @Test
    public void getURL() {
        TwitterAccount ta = createMemberAndAccount("toto", "toto69");
        assertEquals("http://www.twitter.com/toto69", ta.url());
    }
}
