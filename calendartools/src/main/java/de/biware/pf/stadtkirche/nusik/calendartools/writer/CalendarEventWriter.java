/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.writer;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.observer.ConverterProgressObserver;
import de.biware.pf.stadtkirche.nusik.calendartools.observer.ReaderWriterMessageObserver;
import java.io.File;
import java.util.Collection;

/**
 *
 * @author svenina
 */
public interface CalendarEventWriter {
    public void write(Collection<CalendarEvent> events);
    public void useDirectory(File directory);
    public void close();
    public void registerReaderWriterMessageObserver(ReaderWriterMessageObserver observer);
    public void registerConverterProgressObserver(ConverterProgressObserver observer);
}
