/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.result.ViolationResult;

/**
 *
 * @author sbg
 */
public interface CalendarEventValidator {
    public ViolationResult validate(CalendarEvent event);
}
