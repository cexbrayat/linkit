package models.mailing;

/**
 * Members set for mailing.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public enum MembersSet {
    /** All members */
    All,
    /** Registered attendees for the conference */
    Attendees,
    /** Members having not yet registered */
    NonAttendees,
    /** Staff members */
    Staff,
    /** Sponsor members */
    Sponsors,
    /** Member with validated talk */
    Speakers;
}
