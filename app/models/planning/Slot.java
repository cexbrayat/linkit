package models.planning;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A slot is a time period (start - end) when something can be planned.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public enum Slot {

    // Jeudi
    Jeudi_S1_AM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("09:30"), LocalTime.parse("10:30")),
    Jeudi_S2_AM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("09:30"), LocalTime.parse("10:30")),
    Jeudi_S3_AM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("09:30"), LocalTime.parse("10:30")),
    Jeudi_S4_AM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("09:30"), LocalTime.parse("11:00")),
    Jeudi_S5_AM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("09:30"), LocalTime.parse("11:00")),

    Jeudi_S1_AM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("11:00"), LocalTime.parse("12:00")),
    Jeudi_S2_AM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("11:00"), LocalTime.parse("12:00")),
    Jeudi_S3_AM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("11:00"), LocalTime.parse("12:00")),
    Jeudi_S4_AM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("11:00"), LocalTime.parse("12:30")),
    Jeudi_S5_AM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("11:00"), LocalTime.parse("12:30")),

    Jeudi_S1_PM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Jeudi_S2_PM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Jeudi_S3_PM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Jeudi_S4_PM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("13:00"), LocalTime.parse("14:30")),
    Jeudi_S5_PM_1(LocalDate.parse("2013-04-25"), LocalTime.parse("13:00"), LocalTime.parse("14:30")),

    Jeudi_S1_PM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Jeudi_S2_PM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Jeudi_S3_PM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Jeudi_S4_PM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("14:30"), LocalTime.parse("16:00")),
    Jeudi_S5_PM_2(LocalDate.parse("2013-04-25"), LocalTime.parse("14:30"), LocalTime.parse("16:00")),

    Jeudi_S1_PM_3(LocalDate.parse("2013-04-25"), LocalTime.parse("16:30"), LocalTime.parse("17:30")),
    Jeudi_S2_PM_3(LocalDate.parse("2013-04-25"), LocalTime.parse("16:30"), LocalTime.parse("17:30")),
    Jeudi_S3_PM_3(LocalDate.parse("2013-04-25"), LocalTime.parse("16:30"), LocalTime.parse("17:30")),
    Jeudi_S4_PM_3(LocalDate.parse("2013-04-25"), LocalTime.parse("16:00"), LocalTime.parse("19:00")),
    Jeudi_S5_PM_3(LocalDate.parse("2013-04-25"), LocalTime.parse("16:00"), LocalTime.parse("19:00")),

    Jeudi_S1_PM_4(LocalDate.parse("2013-04-25"), LocalTime.parse("18:00"), LocalTime.parse("19:00")),
    Jeudi_S2_PM_4(LocalDate.parse("2013-04-25"), LocalTime.parse("18:00"), LocalTime.parse("19:00")),
    Jeudi_S3_PM_4(LocalDate.parse("2013-04-25"), LocalTime.parse("18:00"), LocalTime.parse("19:00")),

    // Vendredi
    Vendredi_S1_AM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("09:30"), LocalTime.parse("10:30")),
    Vendredi_S2_AM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("09:30"), LocalTime.parse("10:30")),
    Vendredi_S3_AM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("09:30"), LocalTime.parse("10:30")),
    Vendredi_S4_AM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("09:30"), LocalTime.parse("11:00")),
    Vendredi_S5_AM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("09:30"), LocalTime.parse("11:00")),

    Vendredi_S1_AM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("11:00"), LocalTime.parse("12:00")),
    Vendredi_S2_AM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("11:00"), LocalTime.parse("12:00")),
    Vendredi_S3_AM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("11:00"), LocalTime.parse("12:00")),
    Vendredi_S4_AM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("11:00"), LocalTime.parse("12:30")),
    Vendredi_S5_AM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("11:00"), LocalTime.parse("12:30")),

    Vendredi_S1_PM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Vendredi_S2_PM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Vendredi_S3_PM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Vendredi_S4_PM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("13:00"), LocalTime.parse("14:30")),
    Vendredi_S5_PM_1(LocalDate.parse("2013-04-26"), LocalTime.parse("13:00"), LocalTime.parse("14:30")),

    Vendredi_S1_PM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Vendredi_S2_PM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Vendredi_S3_PM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Vendredi_S4_PM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("14:30"), LocalTime.parse("16:00")),
    Vendredi_S5_PM_2(LocalDate.parse("2013-04-26"), LocalTime.parse("14:30"), LocalTime.parse("16:00")),

    Vendredi_S1_PM_3(LocalDate.parse("2013-04-26"), LocalTime.parse("16:30"), LocalTime.parse("18:00")),
    Vendredi_S2_PM_3(LocalDate.parse("2013-04-26"), LocalTime.parse("16:30"), LocalTime.parse("18:00")),
    Vendredi_S3_PM_3(LocalDate.parse("2013-04-26"), LocalTime.parse("16:30"), LocalTime.parse("18:00")),
    Vendredi_S4_PM_3(LocalDate.parse("2013-04-26"), LocalTime.parse("16:00"), LocalTime.parse("18:00")),
    Vendredi_S5_PM_3(LocalDate.parse("2013-04-26"), LocalTime.parse("16:00"), LocalTime.parse("18:00"));

    /**
     * Holds constants for this enum.
     * Constants can't be declared in {@link Slot} enum class H an enum declaration must start with its values, and forward reference is prohibited.
     */
    private static class Const {
        private static final DateTimeFormatter FORMAT = DateTimeFormat.forPattern("k'H'mm");
    }

    private LocalDate day;
    private LocalTime start;
    private LocalTime end;
    
    private Slot(LocalDate day, LocalTime start, LocalTime end) {
        this.day = day;
        this.start = start;
        this.end = end;
    }

    private DateTimeZone TZ_PARIS = DateTimeZone.forID("Europe/Paris");

    public LocalDate getDay() {
        return day;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public DateTime getEndDateTime() {
        return day.toDateTime(end, TZ_PARIS);
    }

    public DateTime getStartDateTime() {
        return day.toDateTime(start, TZ_PARIS);
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
        return day.toString("dd/MM") + " " + start.toString(Const.FORMAT) + " - " + end.toString(Const.FORMAT);
    }
}
