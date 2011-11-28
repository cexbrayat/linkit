package helpers.badge;

import models.Speaker;
import models.Staff;

/**
 * Context holding data regarding a batch of badge computations. Stores cached data used through several computations.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeComputationContext {
    
    /** Number of staff people */
    private Long nbStaff;
    
    /** Number of speaker people */
    private Long nbSpeakers;

    /**
     * @return Number of staff people (computed through DB)
     */
    public Long getNbStaff() {
        if (nbStaff == null) {
            nbStaff = Staff.count();
        }
        return nbStaff;
    }

    /**
     * @return Number of speaker people (computed through DB)
     */
    public Long getNbSpeakers() {
        if (nbSpeakers == null) {
            nbSpeakers = Speaker.count();
        }
        return nbSpeakers;
    }
}
