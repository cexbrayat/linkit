package models.mailing;

import java.util.List;
import models.Member;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryAll implements MembersSetQuery {

    public List<Member> find() {
        return Member.findAll();
    }
}
