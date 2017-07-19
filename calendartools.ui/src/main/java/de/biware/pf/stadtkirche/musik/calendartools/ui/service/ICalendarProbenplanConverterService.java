/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.musik.calendartools.ui.service;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.CalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.DefaultCalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.writer.CalendarEventWriter;
import de.biware.pf.stadtkirche.nusik.calendartools.writer.EnsembleAwareProbenplanICalWriter;
import java.io.File;
import java.util.Collection;

/**
 *
 * @author sbg
 */
public class ICalendarProbenplanConverterService extends AbstractConverterService {

    public ICalendarProbenplanConverterService(boolean shouldValidate) {
        super(shouldValidate);
    }

    @Override
    protected void doConvert(File excelFile, File outputDirectory, String tabellenBlatt) throws ConvertException {
        CalendarEventExcelReader reader
                = new DefaultCalendarEventExcelReader(excelFile, tabellenBlatt, super.isShouldValidate());

        Collection<CalendarEvent> events = reader.read();

        outputDirectory.mkdir();

        CalendarEventWriter writer = new EnsembleAwareProbenplanICalWriter();
        writer.registerReaderWriterMessageObserver(this);
        writer.registerConverterProgressObserver(this);
        writer.useDirectory(outputDirectory);
        writer.write(events);
        writer.close();
    }

}
