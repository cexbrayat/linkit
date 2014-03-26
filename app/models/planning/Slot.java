package models.planning;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static models.planning.SlotConstants.*;

/**
 * A slot is a time period (start - end) when something can be planned.
 * @author Sryl <cyril.lacote@gmail.com>
 */
public enum Slot {

    /**************************************************************************/
    /*******                     2014 slots                           *********/
    /**************************************************************************/
    // Mardi
    //Talks
    Mardi_2014_A1_Talk_1(Mardi_2014, Room.A1, _09_30, _10_20),
    Mardi_2014_A2_Talk_1(Mardi_2014, Room.A2, _09_30, _10_20),
    Mardi_2014_S1_Talk_1(Mardi_2014, Room.S1, _09_30, _10_20),
    Mardi_2014_S2_Talk_1(Mardi_2014, Room.S2, _09_30, _10_20),
    Mardi_2014_A1_Talk_2(Mardi_2014, Room.A1, _10_35, _11_25),
    Mardi_2014_A2_Talk_2(Mardi_2014, Room.A2, _10_35, _11_25),
    Mardi_2014_S1_Talk_2(Mardi_2014, Room.S1, _10_35, _11_25),
    Mardi_2014_S2_Talk_2(Mardi_2014, Room.S2, _10_35, _11_25),
    Mardi_2014_A1_Talk_3(Mardi_2014, Room.A1, _11_40, _12_30),
    Mardi_2014_A2_Talk_3(Mardi_2014, Room.A2, _11_40, _12_30),
    Mardi_2014_S1_Talk_3(Mardi_2014, Room.S1, _11_40, _12_30),
    Mardi_2014_S2_Talk_3(Mardi_2014, Room.S2, _11_40, _12_30),
    Mardi_2014_A1_Talk_4(Mardi_2014, Room.A1, _13_40, _14_30),
    Mardi_2014_A2_Talk_4(Mardi_2014, Room.A2, _13_40, _14_30),
    Mardi_2014_S1_Talk_4(Mardi_2014, Room.S1, _13_40, _14_30),
    Mardi_2014_S2_Talk_4(Mardi_2014, Room.S2, _13_40, _14_30),
    Mardi_2014_A1_Talk_5(Mardi_2014, Room.A1, _14_50, _15_40),
    Mardi_2014_A2_Talk_5(Mardi_2014, Room.A2, _14_50, _15_40),
    Mardi_2014_S1_Talk_5(Mardi_2014, Room.S1, _14_50, _15_40),
    Mardi_2014_S2_Talk_5(Mardi_2014, Room.S2, _14_50, _15_40),
    //Workshops
    Mardi_2014_S3_Workshop_1(Mardi_2014, Room.S3, _09_30, _10_30),
    Mardi_2014_S4_Workshop_1(Mardi_2014, Room.S4, _09_30, _10_30),
    Mardi_2014_S3_Workshop_2(Mardi_2014, Room.S3, _11_40, _13_40),
    Mardi_2014_S4_Workshop_2(Mardi_2014, Room.S4, _11_40, _13_40),
    Mardi_2014_S3_Workshop_3(Mardi_2014, Room.S3, _13_50, _15_50),
    Mardi_2014_S4_Workshop_3(Mardi_2014, Room.S4, _13_50, _15_50),
    //Others
    Mardi_2014_A1_Keynote_1(Mardi_2014, Room.A1, _16_10, _16_30),
    Mardi_2014_A1_Keynote_2(Mardi_2014, Room.A1, _16_30, _16_50),
    Mardi_2014_A1_Keynote_3(Mardi_2014, Room.A1, _16_50, _17_10),
    Mardi_2014_A1_Keynote_4(Mardi_2014, Room.A1, _17_40, _18_10),

    /**************************************************************************/
    /*******                     2013 slots                           *********/
    /**************************************************************************/

    // Jeudi
    Jeudi_Keynote_AM(LocalDate.parse("2013-04-25"), Room.S1, LocalTime.parse("09:00"), LocalTime.parse("09:20")),
    Jeudi_S1_AM_1(LocalDate.parse("2013-04-25"), Room.S1, LocalTime.parse("09:45"), LocalTime.parse("10:45")),
    Jeudi_S2_AM_1(LocalDate.parse("2013-04-25"), Room.S2, LocalTime.parse("09:45"), LocalTime.parse("10:45")),
    Jeudi_S3_AM_1(LocalDate.parse("2013-04-25"), Room.S3, LocalTime.parse("09:45"), LocalTime.parse("10:45")),
    Jeudi_S4_AM_1(LocalDate.parse("2013-04-25"), Room.S4, LocalTime.parse("09:45"), LocalTime.parse("11:15")),
    Jeudi_S5_AM_1(LocalDate.parse("2013-04-25"), Room.S5, LocalTime.parse("09:45"), LocalTime.parse("11:15")),

    Jeudi_S1_AM_2(LocalDate.parse("2013-04-25"), Room.S1, LocalTime.parse("11:15"), LocalTime.parse("12:15")),
    Jeudi_S2_AM_2(LocalDate.parse("2013-04-25"), Room.S2, LocalTime.parse("11:15"), LocalTime.parse("12:15")),
    Jeudi_S3_AM_2(LocalDate.parse("2013-04-25"), Room.S3, LocalTime.parse("11:15"), LocalTime.parse("12:15")),
    Jeudi_S4_AM_2(LocalDate.parse("2013-04-25"), Room.S4, LocalTime.parse("11:15"), LocalTime.parse("12:45")),
    Jeudi_S5_AM_2(LocalDate.parse("2013-04-25"), Room.S5, LocalTime.parse("11:15"), LocalTime.parse("12:45")),

    Jeudi_S1_PM_1(LocalDate.parse("2013-04-25"), Room.S1, LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Jeudi_S2_PM_1(LocalDate.parse("2013-04-25"), Room.S2, LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Jeudi_S3_PM_1(LocalDate.parse("2013-04-25"), Room.S3, LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Jeudi_S4_PM_1(LocalDate.parse("2013-04-25"), Room.S4, LocalTime.parse("13:00"), LocalTime.parse("14:30")),
    Jeudi_S5_PM_1(LocalDate.parse("2013-04-25"), Room.S5, LocalTime.parse("13:00"), LocalTime.parse("14:30")),

    Jeudi_S1_PM_2(LocalDate.parse("2013-04-25"), Room.S1, LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Jeudi_S2_PM_2(LocalDate.parse("2013-04-25"), Room.S2, LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Jeudi_S3_PM_2(LocalDate.parse("2013-04-25"), Room.S3, LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Jeudi_S4_PM_2(LocalDate.parse("2013-04-25"), Room.S4, LocalTime.parse("14:30"), LocalTime.parse("16:00")),
    Jeudi_S5_PM_2(LocalDate.parse("2013-04-25"), Room.S5, LocalTime.parse("14:30"), LocalTime.parse("16:00")),

    Jeudi_S1_PM_3(LocalDate.parse("2013-04-25"), Room.S1, LocalTime.parse("16:30"), LocalTime.parse("17:30")),
    Jeudi_S2_PM_3(LocalDate.parse("2013-04-25"), Room.S2, LocalTime.parse("16:30"), LocalTime.parse("17:30")),
    Jeudi_S3_PM_3(LocalDate.parse("2013-04-25"), Room.S3, LocalTime.parse("16:30"), LocalTime.parse("17:30")),
    Jeudi_S4_PM_3(LocalDate.parse("2013-04-25"), Room.S4, LocalTime.parse("16:00"), LocalTime.parse("19:00")),
    Jeudi_S5_PM_3(LocalDate.parse("2013-04-25"), Room.S5, LocalTime.parse("16:00"), LocalTime.parse("19:00")),

    Jeudi_S1_PM_4(LocalDate.parse("2013-04-25"), Room.S1, LocalTime.parse("18:00"), LocalTime.parse("19:00")),
    Jeudi_S2_PM_4(LocalDate.parse("2013-04-25"), Room.S2, LocalTime.parse("18:00"), LocalTime.parse("19:00")),
    Jeudi_S3_PM_4(LocalDate.parse("2013-04-25"), Room.S3, LocalTime.parse("18:00"), LocalTime.parse("19:00")),

    // Vendredi
    Vendredi_Keynote_AM(LocalDate.parse("2013-04-26"), Room.S1, LocalTime.parse("09:00"), LocalTime.parse("09:20")),
    Vendredi_S1_AM_1(LocalDate.parse("2013-04-26"), Room.S1, LocalTime.parse("09:45"), LocalTime.parse("10:45")),
    Vendredi_S2_AM_1(LocalDate.parse("2013-04-26"), Room.S2, LocalTime.parse("09:45"), LocalTime.parse("10:45")),
    Vendredi_S3_AM_1(LocalDate.parse("2013-04-26"), Room.S3, LocalTime.parse("09:45"), LocalTime.parse("10:45")),
    Vendredi_S4_AM_1(LocalDate.parse("2013-04-26"), Room.S4, LocalTime.parse("09:45"), LocalTime.parse("11:15")),
    Vendredi_S5_AM_1(LocalDate.parse("2013-04-26"), Room.S5, LocalTime.parse("09:45"), LocalTime.parse("11:15")),

    Vendredi_S1_AM_2(LocalDate.parse("2013-04-26"), Room.S1, LocalTime.parse("11:15"), LocalTime.parse("12:15")),
    Vendredi_S2_AM_2(LocalDate.parse("2013-04-26"), Room.S2, LocalTime.parse("11:15"), LocalTime.parse("12:15")),
    Vendredi_S3_AM_2(LocalDate.parse("2013-04-26"), Room.S3, LocalTime.parse("11:15"), LocalTime.parse("12:15")),
    Vendredi_S4_AM_2(LocalDate.parse("2013-04-26"), Room.S4, LocalTime.parse("11:15"), LocalTime.parse("12:45")),
    Vendredi_S5_AM_2(LocalDate.parse("2013-04-26"), Room.S5, LocalTime.parse("11:15"), LocalTime.parse("12:45")),

    Vendredi_S1_PM_1(LocalDate.parse("2013-04-26"), Room.S1, LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Vendredi_S2_PM_1(LocalDate.parse("2013-04-26"), Room.S2, LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Vendredi_S3_PM_1(LocalDate.parse("2013-04-26"), Room.S3, LocalTime.parse("13:30"), LocalTime.parse("14:30")),
    Vendredi_S4_PM_1(LocalDate.parse("2013-04-26"), Room.S4, LocalTime.parse("13:00"), LocalTime.parse("14:30")),
    Vendredi_S5_PM_1(LocalDate.parse("2013-04-26"), Room.S5, LocalTime.parse("13:00"), LocalTime.parse("14:30")),

    Vendredi_S1_PM_2(LocalDate.parse("2013-04-26"), Room.S1, LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Vendredi_S2_PM_2(LocalDate.parse("2013-04-26"), Room.S2, LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Vendredi_S3_PM_2(LocalDate.parse("2013-04-26"), Room.S3, LocalTime.parse("15:00"), LocalTime.parse("16:00")),
    Vendredi_S4_PM_2(LocalDate.parse("2013-04-26"), Room.S4, LocalTime.parse("14:30"), LocalTime.parse("16:00")),
    Vendredi_S5_PM_2(LocalDate.parse("2013-04-26"), Room.S5, LocalTime.parse("14:30"), LocalTime.parse("16:00")),

    Vendredi_S1_PM_3(LocalDate.parse("2013-04-26"), Room.S1, LocalTime.parse("16:30"), LocalTime.parse("18:00")),
    Vendredi_S2_PM_3(LocalDate.parse("2013-04-26"), Room.S2, LocalTime.parse("16:00"), LocalTime.parse("18:00")),
    Vendredi_S3_PM_3(LocalDate.parse("2013-04-26"), Room.S3, LocalTime.parse("16:30"), LocalTime.parse("18:00")),
    Vendredi_S4_PM_3(LocalDate.parse("2013-04-26"), Room.S4, LocalTime.parse("16:00"), LocalTime.parse("18:00")),
    Vendredi_S5_PM_3(LocalDate.parse("2013-04-26"), Room.S5, LocalTime.parse("16:00"), LocalTime.parse("18:00")),
    Vendredi_Keynote_PM(LocalDate.parse("2013-04-26"), Room.S1, LocalTime.parse("18:00"), LocalTime.parse("18:30"));

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
    private Room room;

    private Slot(LocalDate day, Room room, LocalTime start, LocalTime end) {
        this.day = day;
        this.start = start;
        this.end = end;
        this.room = room;
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

    public Room getRoom() {
        return room;
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
