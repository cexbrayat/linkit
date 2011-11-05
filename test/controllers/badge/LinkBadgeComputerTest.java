package controllers.badge;

import java.util.EnumSet;
import java.util.Set;
import models.Badge;
import models.Member;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Unit tests for {@link LinkBadgeComputer}
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class LinkBadgeComputerTest extends UnitTest {

    private LinkBadgeComputer computer = new LinkBadgeComputer();
    
    /** Login of alone member */
    private static final String ALONE = "alone";
    
    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }
    
    @Test
    public void notGranted() {
        final Member alone = Member.findByLogin(ALONE);
        final Set<Badge> actualBadges = computer.compute(alone, new BadgeComputationContext());
        assertTrue(actualBadges.isEmpty());
    }
    
    @Test
    public void grantedLinkator1() {
        final Member alone = Member.findByLogin(ALONE);
        final Member other = Member.all().first();
        // Member links someone
        Member.addLink(alone.login, other.login);
        final Set<Badge> actualBadges = computer.compute(alone, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Linkator1), actualBadges);
    }
    
    @Test
    public void grantedLinkedator1() {
        final Member alone = Member.findByLogin(ALONE);
        final Member other = Member.all().first();
        // Someone links our member
        Member.addLink(other.login, alone.login);
        final Set<Badge> actualBadges = computer.compute(alone, new BadgeComputationContext());
        assertEquals(EnumSet.of(Badge.Linkedator1), actualBadges);
    }
}
