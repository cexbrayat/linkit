package models.mailing;

import java.util.List;

import models.ConferenceEvent;
import models.Member;
import models.Sponsor;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQuerySponsors implements MembersSetQuery {

    public List<Sponsor> find() {
        return Sponsor.findOn(ConferenceEvent.CURRENT);
    }
}
