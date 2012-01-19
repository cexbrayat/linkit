package models;

import play.i18n.Messages;

/**
 *
 * @author agnes007
 * @author Sryl
 */
public enum Track {
    /** agile */
    Agility,
    /** java */
    Techy,
    /** bleeding edge + mixy  */
    Trendy,
    /** coding dojo + agile games */
    Gamy,
    /** web */
    Weby;
    
    private String description;

    private Track() {
        this.description = Messages.get("track."+name()+".description");
    }
    
    public String getDescription() {
        return description;
    }
}
