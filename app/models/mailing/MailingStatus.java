package models.mailing;

/**
 * Status for batch mailing
 * @author Sryl <cyril.lacote@gmail.com>
 */
public enum MailingStatus {
    
    /** Not yet sent */
    Planned,
    /** Currently sending (have been sent to some recipients, but not all) */
    Sending,
    /** Sent to all recipients */
    Sent;
}
