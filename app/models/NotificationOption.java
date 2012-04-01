package models;

/**
 *
 * @author Sryl <cyril.lacote@gmail.com>
 */
public enum NotificationOption {
    
    /* Instant notifications */
    Instant(false),
    /** Hourly notifications */
    Hourly(false),
    /** Daily notifications */
    Daily(false),
    /** Weekly notifications */
    Weekly(true),
    /** No notifications */
    None(false);
    
    /** true if notifications spread on more than one day */
    private boolean moreThanDayLong = false;
    
    private NotificationOption(boolean moreThanDayLong) {
        this.moreThanDayLong = moreThanDayLong;
    }

    public boolean isMoreThanDayLong() {
        return moreThanDayLong;
    }
}
