/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.reader;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEventRowPostProcessor;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.InvalidCalendarEventException;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.result.ViolationResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author svenina
 */
public class DefaultCalendarEventExcelReader implements CalendarEventExcelReader {
    private final File excelFile;
    private final String sheetName;
    private final boolean shouldValidate;
    private final EnsembleDetectionFactory ensembleDetector;
    private boolean hasValidationErrors = false;
    private final List<ViolationResult> validatorMessages = new ArrayList<>();

    public DefaultCalendarEventExcelReader(File excelFile, String sheetName, EnsembleDetectionFactory ensembleDetector) {
        this(excelFile, sheetName, false, ensembleDetector);
    }
    
    public DefaultCalendarEventExcelReader(File excelFile, String sheetName, boolean validate, EnsembleDetectionFactory ensembleDetector) {
        this.excelFile = excelFile;
        this.sheetName = sheetName;
        this.shouldValidate = validate;
        this.ensembleDetector = ensembleDetector;
    }
    
    
    
    @Override
    public File getExcelFile() {
        return this.excelFile;
    }

    @Override
    public String getSheetName() {
        return this.sheetName;
    }

    @Override
    public Collection<CalendarEvent> read() {
        Xcelite xcelite = new Xcelite(this.getExcelFile());
        XceliteSheet sheet = xcelite.getSheet(this.getSheetName());
        // CalendarEvent
        SheetReader<CalendarEvent> reader = sheet.getBeanReader(CalendarEvent.class);
        reader.skipHeaderRow(true);
        reader.addRowPostProcessor(new CalendarEventRowPostProcessor(this, this.ensembleDetector));
        Collection<CalendarEvent> calendarEvents = reader.read();
        
        return calendarEvents;
    }

    @Override
    public boolean hasValidationErrors() {
        return hasValidationErrors;
    }

    @Override
    public void markAsInvalid(ViolationResult ex) {
        this.hasValidationErrors = true;
        this.validatorMessages.add(ex);
    }

    @Override
    public List<ViolationResult> getValidatorMessages() {
        return this.validatorMessages;
    }
    
}
