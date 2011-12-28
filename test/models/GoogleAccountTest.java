package models;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import models.activity.StatusActivity;
import org.junit.Test;

/**
 * Unit tests for {@link GoogleAccount}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class GoogleAccountTest extends BaseDataUnitTest {
    
    protected static StatusActivity buildPost(Member author, String content) {
        return new StatusActivity(author, new Date(), ProviderType.Google, content, null, null);
    }
        
    protected static GoogleAccount createMemberAndAccount(final String login, final String gplusId) {
        GoogleAccount ga = new GoogleAccount(gplusId);
        Member m = new Member(login);
        m.addAccount(ga);
        m.save();
        return ga;
    }

    protected static GoogleAccount createAccount(final String gplusId) {
        return new GoogleAccount(gplusId);
    }
    
    private static String buildMention(String gPlusId, String name) {
        return "<span class=\"proflinkWrapper\"><span class=\"proflinkPrefix\">+</span><a href=\"https://plus.google.com/"+gPlusId+"\" class=\"proflink\" oid=\""+gPlusId+"\">"+name+"</a></span>";
    }
    
    @Test
    public void enhanceMentions() {
        GoogleAccount auteurAccount = createMemberAndAccount("auteur", "1234");
        GoogleAccount mentionnedAccount = createMemberAndAccount("mentioned", "9876");
        final String content1 = "Hey " + buildMention(mentionnedAccount.googleId, mentionnedAccount.member.toString()) + " did you speak about " + buildMention("5434567", "Toto") + " or not?";
        final StatusActivity post1 = buildPost(auteurAccount.member, content1);
        final String content2 = "no mention";
        final StatusActivity post2 = buildPost(auteurAccount.member, content2);
        
        List<StatusActivity> activities = Arrays.asList(post1, post2);
        // Tested method
        auteurAccount.enhance(activities);

        // List preserved
        assertEquals(2, activities.size());
        assertSame(post1, activities.get(0));
        assertSame(post2, activities.get(1));
        // Content enhanced on post1
        assertFalse(content1.equals(post1.content));
        assertTrue(post1.content.contains("href=\"/profile/"+mentionnedAccount.member.login+"\""));
        assertTrue(post1.content.contains(buildMention("5434567", "Toto")));
        // Content same on post2
        assertEquals(content2, post2.content);
    }
    
    @Test
    public void getURL() {
        GoogleAccount ga = createAccount("1234");
        assertEquals("https://profiles.google.com/1234", ga.url());
    }

    @Test
    public void findMemberByGoogleId() {
        final String gplusId = "1234";
        GoogleAccount account = createMemberAndAccount("login", gplusId);
        assertSame(account.member, GoogleAccount.findMemberByGoogleId(gplusId));
        assertNull(GoogleAccount.findMemberByGoogleId("toto"));
    }
}
