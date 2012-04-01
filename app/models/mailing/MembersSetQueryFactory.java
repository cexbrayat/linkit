package models.mailing;

/**
 * Factory of {@link MembersSetQuery}'s implementations
 * @author Sryl <cyril.lacote@gmail.com>
 */
public class MembersSetQueryFactory {

    public static MembersSetQuery create(MembersSet set) {
        MembersSetQuery query = null;
        switch (set) {
            case All:
                query = new MembersSetQueryAll();
                break;
            case Attendees:
                query = new MembersSetQueryAttendees();
                break;
            case NonAttendees:
                query = new MembersSetQueryNonAttendees();
                break;
            case Staff:
                query = new MembersSetQueryStaff();
                break;
            case Sponsors:
                query = new MembersSetQuerySponsors();
                break;
            case Speakers:
                query = new MembersSetQuerySponsors();
                break;
        }
        return query;
    }
}
