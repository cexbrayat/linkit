/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.badge;

import java.util.Set;
import models.Badge;
import models.LinkItAccount;
import models.Member;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

/**
 * Base utility class for unit tests about {@link BadgeComputer} implementations.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class AbstractBadgeComputerTest extends UnitTest {

    protected BadgeComputer computer;
        
    /** One alone member (no links, no linker) */
    protected Member member;

    public AbstractBadgeComputerTest(BadgeComputer computer) {
        this.computer = computer;
    }

    @Before
    public void setUp() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
        member = createMember("alone");
    }

    @After
    public void tearDown() {
        Fixtures.deleteAllModels();
    }

    protected Member createMember(final String login) {
        return new Member(login, new LinkItAccount(login)).save();
    }

    @Test
    public void notGranted() {
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertTrue(actualBadges.isEmpty());
    }
    
}
