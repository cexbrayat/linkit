package models.planning;

import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A slot is a time period (start - end) when something can be planned.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public enum Slot {

    HeightThirtyNine(LocalTime.parse("8H30", Const.FORMAT), LocalTime.parse("9H00", Const.FORMAT)),
    NineTen(LocalTime.parse("9H00", Const.FORMAT), LocalTime.parse("10H00", Const.FORMAT)),
    TenEleven(LocalTime.parse("10H00", Const.FORMAT), LocalTime.parse("11H00", Const.FORMAT)),
    ElevenNoon(LocalTime.parse("11H00", Const.FORMAT), LocalTime.parse("12H00", Const.FORMAT)),
    MidDayBreak(LocalTime.parse("12H00", Const.FORMAT), LocalTime.parse("14H00", Const.FORMAT)),
    TwoThree(LocalTime.parse("14H00", Const.FORMAT), LocalTime.parse("15H00", Const.FORMAT)),
    ThreeFour(LocalTime.parse("15H00", Const.FORMAT), LocalTime.parse("16H00", Const.FORMAT)),
    FourFive(LocalTime.parse("16H00", Const.FORMAT), LocalTime.parse("17H00", Const.FORMAT));
    
    /**
     * Holds constants for this enum.
     * Constants can't be declared in {@link Slot} enum class H an enum declaration must start with its values, and forward reference is prohibited.
     */
    private static class Const {
        private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("k'H'mm");
    }

    private LocalTime start;
    private LocalTime end;
    
    private Slot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getEnd() {
        return end;
    }

    public LocalTime getStart() {
        return start;
    }
    
    public int getStartMinutesFromMidnight() {
        return Minutes.minutesBetween(LocalTime.MIDNIGHT, start).getMinutes();
    }
    
    public int getEndMinutesFromMidnight() {
        return Minutes.minutesBetween(LocalTime.MIDNIGHT, end).getMinutes();
    }
    
    public Minutes getDuration() {
        return Minutes.minutesBetween(start, end);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(start.toString(Const.FORMAT))
                .append(" - ")
                .append(end.toString(Const.FORMAT))
                .toString();
    }
}
