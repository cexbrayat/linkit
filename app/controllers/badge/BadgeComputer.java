package controllers.badge;

import java.util.Set;
import models.Badge;
import models.Member;

/**
 * Interface for an engine computing badges for a given member
 * @author Sryl <cyril.lacote@gmail.com>
 */
interface BadgeComputer {
    
    /**
     * @param member Member to computed new granted badges
     * @param context Context of badge computation
     * @return Granted badges
     */
    Set<Badge> compute(Member member, BadgeComputationContext context);
}
