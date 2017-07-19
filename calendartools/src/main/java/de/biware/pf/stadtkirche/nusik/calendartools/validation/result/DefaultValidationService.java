/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation.result;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.CalendarEventValidator;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.Jsr303CalendarEventValidator;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author svenina
 */
public class DefaultValidationService implements ValidationService {
    private final CalendarEventValidator validator = new Jsr303CalendarEventValidator();
    

    @Override
    public Collection<ViolationResult> validate(Collection<CalendarEvent> events) {
        Collection<ViolationResult> result = new ArrayList<>();
        
        events.stream().forEach((event) -> {
            result.add(validator.validate(event));
        });
        
        return result;
    }

    @Override
    public Collection<CalendarEvent> filterOutFatalViolations(Collection<ViolationResult> violationResults) {
        Collection<CalendarEvent> filteredResult = new ArrayList<>();
        violationResults.stream().forEach((violationResult) -> {
            if(!violationResult.isFatal()) {
                filteredResult.add(violationResult.getCalendarEvent());
            }
        });
        return filteredResult;
    }
    
}
