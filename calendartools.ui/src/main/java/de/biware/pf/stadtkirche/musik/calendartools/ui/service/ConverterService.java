/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.musik.calendartools.ui.service;

import de.biware.pf.stadtkirche.nusik.calendartools.observer.ConverterProgressObserver;
import de.biware.pf.stadtkirche.nusik.calendartools.observer.ReaderWriterMessageObserver;
import java.io.File;

/**
 *
 * @author svenina
 */
public interface ConverterService extends ReaderWriterMessageObserver, ConverterProgressObserver {
    public void convert(File excelFile, File outputDirectory, String tabellenBlatt) throws ConvertException;
    public void registerReaderWriterMessageObserver(ReaderWriterMessageObserver observer);
    public void registerConverterProgressObserver(ConverterProgressObserver observer);
}
