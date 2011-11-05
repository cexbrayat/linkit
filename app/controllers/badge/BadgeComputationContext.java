package controllers.badge;

import models.Staff;

/**
 * Context holding data regarding a batch of badge computations. Stores cached data used through several computations.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeComputationContext {
    
    /** Number of staff people */
    private Long nbStaff;

    /**
     * @return Number of staff people (computed through DB)
     */
    public Long getNbStaff() {
        if (nbStaff == null) {
            nbStaff = Staff.count();
        }
        return nbStaff;
    }
}
