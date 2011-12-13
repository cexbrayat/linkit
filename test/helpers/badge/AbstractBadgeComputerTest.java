package helpers.badge;

import java.util.Set;
import models.Badge;
import models.BaseDataUnitTest;
import models.Member;
import org.junit.Test;

/**
 * Base utility class for unit tests about {@link BadgeComputer} implementations.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public abstract class AbstractBadgeComputerTest extends BaseDataUnitTest {

    protected BadgeComputer computer;
        
    /** One alone member (no links, no linker) */
    protected Member member;

    public AbstractBadgeComputerTest(BadgeComputer computer) {
        this.computer = computer;
    }

    @Override
    public void setUp() {
        super.setUp();
        member = createMember("alone");
    }

    protected Member createMember(final String login) {
        return new Member(login).save();
    }

    @Test
    public void notGranted() {
        final Set<Badge> actualBadges = computer.compute(member, new BadgeComputationContext());
        assertTrue(actualBadges.isEmpty());
    }
}
