package models.mailing;

import java.util.List;
import models.Member;
import models.Talk;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQuerySpeakers implements MembersSetQuery {

    public List<Member> find() {
        return Talk.findAllSpeakers();
    }
}
