/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.processor;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;

/**
 *
 * @author svenina
 */
public interface CalendarEventManipulatingProcessor {
    CalendarEvent process(CalendarEvent event);
}
