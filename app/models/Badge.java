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
    Attendee;
    
    /**
     * Base folder storing badges image files.
     */
    public static final String BASE_URL = "/public/images/badges/";
    /**
     * Default image file extension for badges.
     */
    public static final String DEFAULT_IMAGE_EXT = ".png";
    
    /**
     * URL of icon illustrating this badge
     */
    private String iconUrl;
    
    /**
     * Use enum.name()+".png" as default filename
     */
    Badge() {
        this.iconUrl = BASE_URL + name().toLowerCase() + ".png";
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
