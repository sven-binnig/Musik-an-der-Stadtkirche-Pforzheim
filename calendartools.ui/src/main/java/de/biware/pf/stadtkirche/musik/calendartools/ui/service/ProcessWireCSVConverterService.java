/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.musik.calendartools.ui.service;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.processor.CalendarEventManipulatingProcessor;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.CalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.DefaultCalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.EnsembleDetectionFactory;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.result.DefaultValidationService;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.result.ValidationService;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.result.ViolationResult;
import de.biware.pf.stadtkirche.nusik.calendartools.writer.CalendarEventArtAwareCalendarEventCSVWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author svenina
 */
public class ProcessWireCSVConverterService extends AbstractConverterService {

    private CalendarEventManipulatingProcessor calendarEventProcessor = null;
    private final Logger logger = Logger.getLogger(this.getClass());
    private final ValidationService validationService = new DefaultValidationService();

    public ProcessWireCSVConverterService(boolean shouldValidate, EnsembleDetectionFactory ensembleDetector) {
        super(shouldValidate, ensembleDetector);
    }

    public void setCalendarEventProcessor(CalendarEventManipulatingProcessor calendarEventProcessor) {
        this.calendarEventProcessor = calendarEventProcessor;
    }

    @Override
    protected void doConvert(File excelFile, File outputDirectory, String tabellenBlatt) throws ConvertException {
        CalendarEventExcelReader reader
                = new DefaultCalendarEventExcelReader(excelFile, tabellenBlatt, super.isShouldValidate(), super.getEnsembleDetector());

        CalendarEventArtAwareCalendarEventCSVWriter writer
                = new CalendarEventArtAwareCalendarEventCSVWriter();
        if (this.calendarEventProcessor != null) {
            writer.setCalendarEventProcessor(calendarEventProcessor);
        }
        writer.registerReaderWriterMessageObserver(this);

        Collection<CalendarEvent> events = reader.read();
        Collection<ViolationResult> validationResult = this.validationService.validate(events);
        Collection<CalendarEvent> filteredEvents = this.validationService.filterOutFatalViolations(validationResult);
        this.logViolations(validationResult, filteredEvents, writer);
        
        outputDirectory.mkdir();

        writer.useDirectory(outputDirectory);
        writer.registerConverterProgressObserver(this);
        writer.write(filteredEvents);
        writer.close();
    }

    private void logViolations(Collection<ViolationResult> validationResult, Collection<CalendarEvent> filteredEvents,  CalendarEventArtAwareCalendarEventCSVWriter writer) {
        validationResult.stream().forEach((result) -> {
            result.getFailures().stream().forEach((failure) -> {
                writer.print(failure.getViolationSeverity().name() + " " + failure.getMessage());
            });
        });
        writer.print("-- herausgefiltert: " + (validationResult.size() - filteredEvents.size()) + " Elemente.");
    }
}
