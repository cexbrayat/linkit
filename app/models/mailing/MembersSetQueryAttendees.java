package models.mailing;

import java.util.List;
import models.Member;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryAttendees implements MembersSetQuery {

    public List<Member> find() {
        return Member.find("ticketingRegistered=true").fetch();
    }
}
