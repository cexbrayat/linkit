package models;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import models.activity.StatusActivity;
import org.junit.Test;

/**
 * Unit tests for {@link TwitterAccount}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TwitterAccountTest extends BaseDataUnitTest {
    
    protected static StatusActivity buildTweet(Member author, String content) {
        return new StatusActivity(author, new Date(), ProviderType.Twitter, content, null, null);
    }
    
    protected static TwitterAccount createMemberAndAccount(final String login, final String twitterName) {
        TwitterAccount ta = new TwitterAccount(twitterName);
        Member m = new Member(login);
        m.addAccount(ta);
        m.save();
        return ta;
    }
    
    @Test
    public void enhanceMentions() {
        TwitterAccount auteurAccount = createMemberAndAccount("auteur", "leplusgrandauteur");
        TwitterAccount mentionnedAccount = createMemberAndAccount("mentioned","mentioned");
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
        assertTrue(tweet1.content.contains("href=\"/profile/"+mentionnedAccount.member.login+"\""));
        assertTrue(tweet1.content.contains("<a href=\"http://www.twitter.com/toto\" target=\"_new\">@toto</a>"));
        // Content same on tweet2
        assertEquals(content2, tweet2.content);
    }
    
    @Test
    public void enhanceURLs() {
        TwitterAccount auteurAccount = createMemberAndAccount("auteur", "leplusgrandauteur");
        final String URL1 = "http://www.toto.com/toto/123?567=891&tgh";
        final String URL2 = "http://goo.gl/1gTh5+";
        final String content1 = "Hey check this out : " + URL1 + " & " + URL2 + " incroyable!";
        final StatusActivity tweet1 = buildTweet(auteurAccount.member, content1);
        final String content2 = "Je tweete sans URL!";
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
        assertTrue(tweet1.content.contains("<a href=\""+URL1+"\" target=\"_new\">"+URL1+"</a>"));
        assertTrue(tweet1.content.contains("<a href=\""+URL2+"\" target=\"_new\">"+URL2+"</a>"));
        // Content same on tweet2
        assertEquals(content2, tweet2.content);
    }
    
    @Test
    public void getURL() {
        TwitterAccount ta = createMemberAndAccount("toto", "toto69");
        assertEquals("http://www.twitter.com/toto69", ta.url());
    }
    
    @Test
    public void findMemberByScreenName() {
        final String screenName = "screen";
        TwitterAccount account = createMemberAndAccount("login", screenName);
        assertSame(account.member, TwitterAccount.findMemberByScreenName(screenName));
        assertNull(TwitterAccount.findMemberByScreenName("toto"));
    }
}
