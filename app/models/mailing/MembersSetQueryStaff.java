package models.mailing;

import java.util.List;
import models.Member;
import models.Staff;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryStaff implements MembersSetQuery {

    public List<Member> find() {
        return Staff.findAll();
    }
}
