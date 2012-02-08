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

    private LightningTalk createLT(String title, Member... speakers) {
        LightningTalk lt = new LightningTalk();
        lt.title = title;
        for (Member s : speakers) {
            lt.addSpeaker(s);
        }
        return lt.save();
    }
    
    @Test public void getNumberOfVotes() {
        final LightningTalk lt = createLT("test");
        assertEquals(0, lt.getNumberOfVotes());
        
        // 2 Votes
        final Member m = createMember("toto");
        new Vote(lt, m, true).save();
        new Vote(lt, m, true).save();

        assertEquals(2, lt.getNumberOfVotes());
    }
    
    @Test public void hasVoteFrom() {
        final LightningTalk lt = createLT("test");
        final Member m = createMember("toto");
        assertFalse(lt.hasVoteFrom(m.login));
        
        // 1 Vote
        new Vote(lt, m, true).save();

        assertTrue(lt.hasVoteFrom(m.login));
    }
    
    @Test public void getShowUrl() {
        final LightningTalk t = createLT("test");
        assertNotNull(t.getShowUrl());
    }
        
    @Test public void delete() {
        Member speaker1 = createMember("speaker1");
        Member speaker2 = createMember("speaker2");
        LightningTalk lt = createLT("test", speaker1, speaker2);
        // Some comments
        Member member1 = createMember("member1");
        Member member2 = createMember("member2");
        lt.addComment(new SessionComment(member1, lt, "Comentaire"));
        lt.addComment(new SessionComment(member2, lt, "Comentaire"));
        // Some votes
        new Vote(lt, member1, true).save();
        new Vote(lt, member2, false).save();
        lt.save();
        
        assertNotNull(lt.delete());
        assertNull(LightningTalk.findById(lt.id));
    }
}
