package models;

/**
 * A badge rewarding some kind of awesome activity on LinkIT
 * @author Sryl <cyril.lacote@gmail.com>
 */
public enum Badge {

    /** A speaker member */
    Speaker,
    /** A staff member */
    Staff,
    /** A sponsor member */
    Sponsor,
    /** A registered attendee */
    Attendee,
    /** A friend of all staff members */
    StaffFriend,
    /** A friend of all speaker members */
    SpeakerFriend,
    /** Having made one comment */
    Commentator1,
    /** Having made 5 comments */
    Commentator5,
    /** Having linked 1 member */
    Linkator1,
    /** Having linked 5 members */
    Linkator5,
    /** Being linked by 1 member */
    Linkedator1,
    /** Being linked by 5 members */
    Linkedator5;

    /**
     * Base folder storing badges image files.
     */
    private static final String BASE_URL = "/public/images/badges/";
    /**
     * Default image file extension for badges.
     */
    private static final String DEFAULT_IMAGE_EXT = ".png";
    
    /**
     * URL of icon illustrating this badge
     */
    private String iconUrl;
    
    /**
     * Use enum.name()+".png" as default filename
     */
    Badge() {
        this.iconUrl = BASE_URL + name().toLowerCase() + DEFAULT_IMAGE_EXT;
    }
    
    /**
     * @param iconFileName The filename of the image file
     */
    Badge(final String iconFileName) {
        this.iconUrl = BASE_URL + iconFileName;
    }

    public String getIconUrl() {
        return iconUrl;
    }
    
}
