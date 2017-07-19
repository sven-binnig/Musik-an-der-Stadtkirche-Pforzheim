/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.musik.calendartools.ui.service;

import de.biware.pf.stadtkirche.nusik.calendartools.observer.ConverterProgressObserver;
import de.biware.pf.stadtkirche.nusik.calendartools.observer.ReaderWriterMessageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author sbg
 */
public abstract class AbstractConverterService implements ConverterService {

    //private Collection<ReaderWriterMessageObserver> observers = new ArrayList<>();
    private final Collection<ReaderWriterMessageObserver> messageObservers = new ArrayList<>();
    private final Collection<ConverterProgressObserver> progressOberservers = new ArrayList<>();
    private final boolean shouldValidate;

    public AbstractConverterService(boolean shouldValidate) {
        this.shouldValidate = shouldValidate;
    }

    protected boolean isShouldValidate() {
        return shouldValidate;
    }
    
    

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
        progressOberservers.forEach((observer) -> {
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

    @Override
    public void setCompleteCount(int count) {
        this.fireMaximumCount(count);
    }

    @Override
    public void onAfterContert() {
        this.fireOnAfterConvert();
    }

    @Override
    public void onBeforeConvert() {
        this.fireOnBeforeConvert();
    }

    @Override
    public void onMessage(String from, String message) {
        this.fireReaderWriterMessage(from, message);
    }

    @Override
    public final void convert(File excelFile, File outputDirectory, String tabellenBlatt) throws ConvertException {
        try {
            doConvert(excelFile, outputDirectory, tabellenBlatt);
        } catch (Throwable t) {
            throw new ConvertException(t);

        }
    }

    protected abstract void doConvert(File excelFile, File outputDirectory, String tabellenBlatt) throws ConvertException;
}
