package models;

/**
 * A badge rewarding some kind of awesome activity on LinkIT.
 * Every badge MUST be displayable with an icon, either with an image file URL {@link Badge#iconUrl} OR with an Unicode character {@link Badge#iconChar}.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public enum Badge {

    /** A speaker member */
    Speaker('\u2655'),
    /** A staff member */
    Staff,
    /** A sponsor member */
    Sponsor,
    /** A registered attendee */
    Attendee,
    /** A friend of all staff members */
    StaffBestFriend,
    /** A friend of all speaker members */
    SpeakerFan,
    /** A friend of all sponsor members */
    SponsorFriendly,
    /** Having made one comment */
    Brave,
    /** Having made 10 comments */
    Troller,
    /** Having linked 1 member */
    NewBorn,
    /** Having linked 10 members */
    Friendly,
    /** Having linked 50 members */
    SocialBeast,
    /** Having linked 100 members */
    MadLinker,
    /** Being linked by 1 member */
    YouReNotAlone,
    /** Being linked by 10 members */
    LocalCelebrity,
    /** Being linked by 50 members */
    RockStar,
    /** Being linked by 100 members */
    Leader,
    /** Being linked by 200 members */
    Idol,
    /** Having tweeted about Mix-IT */
    Twittos('t'),
    /** Being posted on Google+ about Mix-IT */
    Plusoner('+');

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
     * Single unicode char illustrating badge
     */
    private Character iconChar;
    
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

    Badge(final char iconChar) {
        this.iconChar = iconChar;
    }
    
    public String getIconUrl() {
        return iconUrl;
    }

    public Character getIconChar() {
        return iconChar;
    }
    
}
