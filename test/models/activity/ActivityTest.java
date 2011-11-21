package models.activity;

import java.util.EnumSet;
import java.util.List;
import models.LinkItAccount;
import models.Member;
import models.ProviderType;
import models.Session;
import org.junit.*;

/**
 * Unit tests for {@link Activity} domain object
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class ActivityTest extends AbstractActivityTest {

    @Test
    public void recents() {
        assertNotNull(Activity.recents(1, 10));
    }

    @Test
    public void recentDatesByMember() {
        assertNotNull(Activity.recentDatesByMember(member, 1, 10));
    }

    @Test
    public void recentsByMember() {
        final Member m = Member.all().first();
        assertNotNull(Activity.recentsByMember(m,EnumSet.allOf(ProviderType.class), 1, 10));
    }
    
    @Test
    public void recentsByMemberWithoutProvider() {
        final Member m = Member.all().first();
        // if none provider ==> same behavior if all providers are selected
        assertEquals(Activity.recentsByMember(m,EnumSet.allOf(ProviderType.class), 1, 10),
                Activity.recentsByMember(m,EnumSet.noneOf(ProviderType.class), 1, 10));
    }
    @Test
    public void recentsForMember() {
        List<Member> members = Member.all().fetch();
        Member m = members.get(0);
        // Ensure existing links
        m.addLink(members.get(1));
        assertNotNull(Activity.recentsForMember(m, EnumSet.allOf(ProviderType.class),1, 10));
    }
    
    @Test
    public void recentsForMemberWithoutProvider() {
        List<Member> members = Member.all().fetch();
        Member m = members.get(0);
        // Ensure existing links
        m.addLink(members.get(1));
        // if none provider ==> same behavior if all providers are selected
        assertEquals(Activity.recentsForMember(m, EnumSet.allOf(ProviderType.class),1, 10),
                Activity.recentsForMember(m, EnumSet.noneOf(ProviderType.class),1, 10));
    }

    @Test
    public void recentsForMemberNoLinks() {
        Member nolinks = new Member("toto");
        List<Activity> activities = Activity.recentsForMember(nolinks,EnumSet.allOf(ProviderType.class), 1, 10);
        assertNotNull(activities);
        assertTrue(activities.isEmpty());
    }

    @Test
    public void recentsBySession() {
        final Session s = Session.all().first();
        assertNotNull(Activity.recentsBySession(s, 1, 10));
    }

    @Test
    public void uncomputed() {
        assertNotNull(Activity.uncomputed());
    }
}
