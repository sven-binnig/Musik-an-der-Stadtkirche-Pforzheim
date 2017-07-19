/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.writer;

import de.biware.pf.stadtkirche.nusik.calendartools.observer.ConverterProgressObserver;
import de.biware.pf.stadtkirche.nusik.calendartools.observer.ReaderWriterMessageObserver;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author sbg
 */
public abstract class AbstractCalendarEventWriter implements CalendarEventWriter {

    private final Collection<ReaderWriterMessageObserver> messageObservers = new ArrayList<>();
    private final Collection<ConverterProgressObserver> progressOberservers = new ArrayList<>();

    @Override
    public void registerReaderWriterMessageObserver(ReaderWriterMessageObserver observer) {
        this.messageObservers.add(observer);
    }
    
    @Override
    public void registerConverterProgressObserver(ConverterProgressObserver observer) {
        this.progressOberservers.add(observer);
    }
    
    protected void fireReaderWriterMessage(String from, String message) {
        messageObservers.forEach((observer) -> {
            observer.onMessage(from, message);
        });
    }
    
    protected void fireOnBeforeConvert() {
        progressOberservers.forEach((observer) ->  {
            observer.onBeforeConvert();
        });
    }
    
    protected void fireOnAfterConvert() {
        progressOberservers.forEach((observer) -> {
            observer.onAfterContert();
        });
    }
    
    protected void fireMaximumCount(int count) {
        progressOberservers.forEach((observer) -> {
            observer.setCompleteCount(count);
        });
    }
            
}
