/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

import de.biware.pf.stadtkirche.nusik.calendartools.reader.CalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.DefaultCalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.ExcelEnsembleDetectionFactory;
import java.io.File;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author svenina
 */
public class CalendarEventAndEnsembleFromExcelReaderTest {
    @Test
    public void excelWithCalendarEventsAndEnsemblesCanBeReadSuccessfully() {
        File excel = new File("src/test/resources/Gesamtprobenplan_und_ensembles.xlsx");
        CalendarEventExcelReader reader
                = new DefaultCalendarEventExcelReader(excel, "CD_2017-12-22", new ExcelEnsembleDetectionFactory(excel, "Ensembles"));
        
        Collection<CalendarEvent> events = reader.read();
        Assert.assertEquals(682, events.size());
    }
}
