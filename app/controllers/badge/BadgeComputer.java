package controllers.badge;

import java.util.Set;
import models.Badge;
import models.Member;

/**
 * Interface for an engine computing badges for a given member
 * @author Sryl <cyril.lacote@gmail.com>
 */
public interface BadgeComputer {
    
    /**
     * Computes badges to be granted to given member
     * @param member Member to compute new granted badges
     * @param context Context of badge computation
     * @return Granted badges, potentially empty
     */
    Set<Badge> compute(Member member, BadgeComputationContext context);
}
