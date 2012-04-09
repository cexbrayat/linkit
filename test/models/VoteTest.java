package models;

import java.util.Arrays;
import org.junit.*;
import java.util.Collections;

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

    private Talk createTalk() {
        return new Talk().save();
    }

    @Test public void findNumberOfVotesBySession() {
        assertEquals(0, Vote.findNumberOfVotesBySession(createLT()));
    }

    @Test public void findVotersBySession() {
        Talk t = createTalk();
        assertEquals(Collections.emptyList(), Vote.findVotersBySession(t));
        
        Member m1 = createMember("member1");
        Member m2 = createMember("member2");
        Member m3 = createMember("member3");
        new Vote(t, m1, true).save();
        new Vote(t, m2, false).save();
        new Vote(t, m3, true).save();

        Talk otherTalk = createTalk();
        Member otherMember = createMember("other");
        new Vote(otherTalk, otherMember, true).save();
        new Vote(otherTalk, m2, true).save();
        new Vote(otherTalk, m3, true).save();
        
        assertEquals(Arrays.asList(m1, m3), Vote.findVotersBySession(t));
    }

    @Test public void findFavoriteTalksByMember() {
        Member m = createMember("member");
        assertEquals(Collections.emptyList(), Vote.findFavoriteTalksByMember(m));
        
        Talk t1 = createTalk();
        LightningTalk lt1 = createLT();
        Talk t2 = createTalk();
        LightningTalk lt2 = createLT();
        new Vote(t1, m, true).save();
        new Vote(lt1, m, false).save();
        new Vote(t2, m, true).save();
        new Vote(lt2, m, true).save();
        
        assertEquals(Arrays.asList(t1, t2), Vote.findFavoriteTalksByMember(m));
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
