package models;

import org.junit.*;

/**
 * Unit tests for {@link Vote} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class VoteTest extends BaseDataUnitTest {

    private Member createMember(String login) {
        return new Member(login).save();
    }

    private LightningTalk createLT() {
        return new LightningTalk().save();
    }
    
    @Test public void findNumberOfVotesBySession() {
        assertEquals(0, Vote.findNumberOfVotesBySession(createLT()));
    }
    
    @Test public void countVotesByMember() {
        assertEquals(0, Vote.countVotesByMember(createMember("toto")));
    }
    
    @Test public void findVote() {
        assertNull(Vote.findVote(createLT(), createMember("toto")));
    }
    
    @Test public void deleteForMember() {
        Member m = createMember("toto");
        LightningTalk lt = createLT();
        new Vote(lt, m, true).save();
        assertEquals(1, Vote.countVotesByMember(m));
        
        Vote.deleteForMember(m);
        assertEquals(0, Vote.countVotesByMember(m));
    }
    
    @Test public void deleteForSession() {
        Member m = createMember("toto");
        LightningTalk lt = createLT();

        new Vote(lt, m, true).save();
        assertEquals(1, Vote.findNumberOfVotesBySession(lt));
        
        Vote.deleteForSession(lt);
        assertEquals(0, Vote.findNumberOfVotesBySession(lt));
    }
}
