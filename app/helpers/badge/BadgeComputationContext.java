package helpers.badge;

import models.Sponsor;
import models.Staff;
import models.Talk;

/**
 * Context holding data regarding a batch of badge computations. Stores cached data used through several computations.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class BadgeComputationContext {
    
    /** Number of staff people */
    private Long nbStaff;

    /** Number of speaker people */
    private Long nbSpeakers;

    /** Number of sponsors people */
    private Long nbSponsors;

    /**
     * @return Number of staff people (computed through DB)
     */
    public long getNbStaff() {
        if (nbStaff == null) {
            nbStaff = Staff.count();
        }
        return nbStaff == null ? 0L : nbStaff.longValue();
    }

    /**
     * @return Number of speaker people (computed through DB)
     */
    public long getNbSpeakers() {
        if (nbSpeakers == null) {
            nbSpeakers = Talk.countSpeakers();
        }
        return nbSpeakers == null ? 0L : nbSpeakers.longValue();
    }

    public long getNbSponsors() {
        if (nbSponsors == null) {
            // FIXME CLA Count sponsors on current event
            nbSponsors = Sponsor.count();
        }
        return nbSponsors == null ? 0L : nbSponsors.longValue();
    }
}
