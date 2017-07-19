/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.processor;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.utils.DateUtils;
import java.util.Date;


/**
 *
 * @author svenina
 */
public class DateCalendarEventManipulatingProcessor implements CalendarEventManipulatingProcessor {

    private final long daysToMove;

    public DateCalendarEventManipulatingProcessor(long daysToMove) {
        this.daysToMove = daysToMove;
    }
    
    
    
    @Override
    public CalendarEvent process(CalendarEvent event) {
        Date beginn = event.getBeginnDatum();
        Date ende = event.getEndeDatum();
        
        event.setBeginnDatum(DateUtils.add(beginn, daysToMove));
        event.setEndeDatum(DateUtils.add(ende,daysToMove));
        
        return event;
    }
    
}
