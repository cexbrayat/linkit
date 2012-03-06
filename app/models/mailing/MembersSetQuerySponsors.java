package models.mailing;

import java.util.List;
import models.Member;
import models.Sponsor;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQuerySponsors implements MembersSetQuery {

    public List<Member> find() {
        return Sponsor.findAll();
    }
}
