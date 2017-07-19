/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation.result;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.ViolationSeverity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author svenina
 */
public class ViolationResult {
    private final List<ValidationFailure> failures = new ArrayList<>();
    private final CalendarEvent calendarEvent;

    public ViolationResult(CalendarEvent calendarEvent) {
        this.calendarEvent = calendarEvent;
    }

    public CalendarEvent getCalendarEvent() {
        return calendarEvent;
    }
    
    

    public List<ValidationFailure> getFailures() {
        return failures;
    }
    
    public boolean isFatal() {
        if (failures.stream().anyMatch((failure) -> (failure.getViolationSeverity().compareTo(ViolationSeverity.ABBRUCH) == 0))) {
            return true;
        }
        return false;
    }   
}
