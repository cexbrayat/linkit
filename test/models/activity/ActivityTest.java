package models.activity;

import com.sun.corba.se.spi.oa.OADefault;
import java.util.EnumSet;
import java.util.List;
import models.Article;
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
    public void recentsByArticle() {
        final Article a = Article.all().first();
        assertNotNull(Activity.recentsByArticle(a, 1, 10));
    }

    @Test
    public void uncomputedIds() {
        assertNotNull(Activity.uncomputedIds());
    }
    
    @Test
    public void deleteForMember() {
        assertNotNull(Activity.deleteForMember(member));
    }
    
    @Test
    public void deleteForArticle() {
        final Article article = Article.all().first();
        assertNotNull(Activity.deleteForArticle(article));
    }
    
    @Test
    public void deleteForSession() {
        final Session session = Session.all().first();
        assertNotNull(Activity.deleteForSession(session));
    }
    
    @Test
    public void deleteForMemberWithProvider() {
        assertNotNull(Activity.deleteForMember(member, ProviderType.Twitter));
    }
    
    @Test public void findOrderedMembers() {
        final Member member1 = createMember("member1");
        final Member member2 = createMember("member2");
        final Member member3 = createMember("member3");
        final Member member4 = createMember("member4");

        assertEquals(0, Activity.find("from Activity a where a.member is not null").fetch().size());
        Activity.OrderedMembersDTO members = Activity.findOrderedMembers();
        // member2 n'est pas le premier de la liste
        assertNotSame(member2, members.getMembers().get(0));
        assertNull(members.getLatestActivityFor(member2));
        final int nbMembers = members.getMembers().size();
        
        // Création d'une activité pour membre1
        new LinkActivity(member1, member2).save();
        // puis membre3
        new SignUpActivity(member3).save();
        // puis membre 2 (qui devient donc le dernier actif en date)
        new LookProfileActivity(member2, member1).save();
        
        members = Activity.findOrderedMembers();
        // member2 EST le premier de la liste
        assertSame(member2, members.getMembers().get(0));
        // il a une date de dernière activité
        assertNotNull(members.getLatestActivityFor(member2));
        // Et on a toujours autant de membres retournés, qu'il existe des activités ou non.
        assertEquals(nbMembers, members.getMembers().size());
    }
}
