package models.mailing;

import com.google.common.collect.Sets;
import java.util.Set;
import models.BaseDataUnitTest;
import models.Member;
import org.junit.Test;

/**
 * Unit tests for {@link Mailing}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MailingTest extends BaseDataUnitTest {
    
    @Test public void recents() {
        assertNotNull(Mailing.recents(1, 10));
    }
    
    @Test public void pendings() {
        assertNotNull(Mailing.pending());
    }
    
    final Member createMember(String login) {
        return new Member(login).save();
    }
    
    @Test public void test() {
        final Member member1 = createMember("login1");
        final Member member2 = createMember("login2");
        final Member member3 = createMember("login3");
        final Mailing m = new Mailing();
        m.recipients = MembersSet.All;
        m.actualRecipients = Sets.newHashSet(member1, member3);
        
        final Set<Member> pendings = m.getPendingRecipients();
        assertFalse(pendings.contains(member1));
        assertTrue(pendings.contains(member2));
        assertFalse(pendings.contains(member3));
    }
}
