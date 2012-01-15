package models;

import java.util.Collections;
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
}
