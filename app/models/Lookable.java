package models;

/**
 * Interface for lookable entities whose displays are counted
 * @author Sryl <cyril.lacote@gmail.com>
 */
public interface Lookable {
    
    /** Number of looks */
    public long getNbLooks();
    
    /** Entity looked by given member (optional, may be null if not known). */
    public void lookedBy(Member member);
    
}
