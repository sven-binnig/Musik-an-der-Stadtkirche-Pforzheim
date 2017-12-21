/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author svenina
 */
public class DateUtils {

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    // ---------------
    
    public static Date add(Date date, long days) {
        LocalDate ld = asLocalDate(date).plusDays(days);
        return asDate(ld);
    }
    
    public static boolean dateSuitsToWeekday(Date date, String weekday) {
        LocalDate ld = asLocalDate(date);
        int dayOfWeek = ld.getDayOfWeek().getValue();
        //System.out.println(ld.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY));
        String[] dow = {"-", "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
        
        return weekday.startsWith(dow[dayOfWeek]);
    }
}
