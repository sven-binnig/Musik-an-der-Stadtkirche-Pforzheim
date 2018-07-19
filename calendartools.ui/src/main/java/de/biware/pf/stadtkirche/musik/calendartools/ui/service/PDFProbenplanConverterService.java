/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.musik.calendartools.ui.service;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.CalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.DefaultCalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.EnsembleDetectionFactory;
import de.biware.pf.stadtkirche.nusik.calendartools.writer.CalendarEventWriter;
import de.biware.pf.stadtkirche.nusik.calendartools.writer.EnsembleAwareProbenplanPDFWriter;
import java.io.File;
import java.util.Collection;

/**
 *
 * @author svenina
 */
public class PDFProbenplanConverterService extends AbstractConverterService {

    public PDFProbenplanConverterService(boolean shouldValidate, EnsembleDetectionFactory ensembleDetector) {
        super(shouldValidate, ensembleDetector);
    }

    @Override
    protected void doConvert(File excelFile, File outputDirectory, String tabellenBlatt) throws ConvertException {
        CalendarEventExcelReader reader
                = new DefaultCalendarEventExcelReader(excelFile, tabellenBlatt, super.isShouldValidate(), super.getEnsembleDetector());
 
               
        Collection<CalendarEvent> events = reader.read();
        
        outputDirectory.mkdir();
        
        CalendarEventWriter writer = new EnsembleAwareProbenplanPDFWriter();
        writer.registerReaderWriterMessageObserver(this);
        writer.registerConverterProgressObserver(this);
        writer.useDirectory(outputDirectory);
        writer.write(events);
        writer.close();
    }
    
}
