/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.reader;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.result.ViolationResult;
import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author svenina
 */
public interface CalendarEventExcelReader {
    public File getExcelFile();
    public String getSheetName();
    public Collection<CalendarEvent> read();
    public boolean hasValidationErrors();
    public void markAsInvalid(ViolationResult ex);
    public List<ViolationResult> getValidatorMessages();
    
}
