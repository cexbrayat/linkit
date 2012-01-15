package models;

import org.junit.*;

/**
 * Unit tests for {@link Session} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 * @author Agnes <agnes.crepet@gmail.com>
 */
public class LightningTalkTest extends BaseDataUnitTest {

    private Member createMember(String login) {
        return new Member(login).save();
    }

    private LightningTalk createLT(Member... speakers) {
        LightningTalk lt = new LightningTalk();
        for (Member s : speakers) {
            lt.addSpeaker(s);
        }
        return lt.save();
    }
    
    @Test public void getNumberOfVotes() {
        final LightningTalk lt = createLT();
        assertEquals(0, lt.getNumberOfVotes());
        
        // 2 Votes
        final Member m = createMember("toto");
        new Vote(lt, m, true).save();
        new Vote(lt, m, true).save();

        assertEquals(2, lt.getNumberOfVotes());
    }
    
    @Test public void hasVoteFrom() {
        final LightningTalk lt = createLT();
        final Member m = createMember("toto");
        assertFalse(lt.hasVoteFrom(m.login));
        
        // 1 Vote
        new Vote(lt, m, true).save();

        assertTrue(lt.hasVoteFrom(m.login));
    }
    
    @Test public void getShowUrl() {
        final LightningTalk t = createLT();
        assertNotNull(t.getShowUrl());
    }
}
