package models.mailing;

import java.util.List;
import models.Member;
import models.Talk;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryFailedSpeakers implements MembersSetQuery {

    public List<Member> find() {
        return Talk.find("select distinct t.speakers from Talk t where t.valid=false").fetch();
    }
}
