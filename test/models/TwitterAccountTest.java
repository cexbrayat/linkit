package models;

import org.junit.Test;
import play.test.UnitTest;

/**
 * Unit tests for {@link TwitterAccount} 
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class TwitterAccountTest extends UnitTest {
    
    @Test
    public void testInitMemberProfileNull() {
        new TwitterAccount(null, null).initMemberProfile();
    }
    
    @Test
    public void testInitMemberProfileOK() {
        final TwitterAccount ta = new TwitterAccount(null, null);
        ta.name = "Jean Dupont";
        ta.screenName = "jean_dupont";
        
        Member m = new Member("login", ta);
        ta.initMemberProfile();
        
        assertEquals(ta.name, m.displayName);
        assertEquals(ta.screenName, m.twitterName);
    }
}
