/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

import de.biware.pf.stadtkirche.nusik.calendartools.test.TemporaryExcelFile;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.CalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.DefaultCalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.test.NameBasedEnsembleDetectionFactory;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 *
 * @author sbg
 */
public class ExcelReaderTests {

    @Rule
    public TemporaryExcelFile excelFile = new TemporaryExcelFile();

    @Test
    public void uhrzeitImFalschenFormatWurdeKorrigiert() throws URISyntaxException {
        File xls = this.excelFile.getDefaultExcelFile();

        CalendarEventExcelReader reader
                = new DefaultCalendarEventExcelReader(xls, "Tabelle1", new NameBasedEnsembleDetectionFactory());

        Collection<CalendarEvent> events = reader.read();
        for (CalendarEvent event : events) {
            if (event.getBeginnUhrzeit() != null) {
                int dotIndex = event.getBeginnUhrzeit().indexOf('.');
                Assert.assertNotEquals(3, dotIndex);
                Assert.assertNotEquals(4, dotIndex);
            }
            
            if(event.getEndeUhrzeit() != null) {
                int dotIndex = event.getEndeUhrzeit().indexOf('.');
                Assert.assertNotEquals(3, dotIndex);
                Assert.assertNotEquals(4, dotIndex);
            }
        }
    }
 
    
}
