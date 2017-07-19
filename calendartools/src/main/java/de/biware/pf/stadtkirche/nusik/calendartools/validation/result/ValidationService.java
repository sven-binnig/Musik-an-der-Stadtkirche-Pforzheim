/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation.result;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import java.util.Collection;

/**
 *
 * @author svenina
 */
public interface ValidationService {
    Collection<ViolationResult> validate(Collection<CalendarEvent> event);
    Collection<CalendarEvent> filterOutFatalViolations(Collection<ViolationResult> violationResults);
}
