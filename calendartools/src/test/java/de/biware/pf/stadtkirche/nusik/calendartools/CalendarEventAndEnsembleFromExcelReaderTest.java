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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
    
    @Test
    public void excelWithCalendarEventsAndEnsemblesDectectsCorrectEnsembles() {
        File excel = new File("src/test/resources/Gesamtprobenplan_und_ensembles.xlsx");
        CalendarEventExcelReader reader
                = new DefaultCalendarEventExcelReader(excel, "Tabelle1", new ExcelEnsembleDetectionFactory(excel, "Ensembles"));
        
        Collection<CalendarEvent> events = reader.read();
        Assert.assertEquals(2, events.size());
        events.stream().forEach((event) -> {
            if(event.getBeschreibung().equals("Atempause im Advent")) {
                Assert.assertTrue(event.getArt().equals("Konzert"));
            } else if(event.getBeschreibung().equals("Generalprobe Krippenspiel")) {
                Assert.assertTrue(event.getArt().equals("Probe"));
                List<Ensemble> ensembles = event.getEnsembles();
                Assert.assertEquals(4, ensembles.size());
                Collection<String> names = Arrays.asList("Singschulklasse I/II","Kleine Kurrende","Große Kurrende Jungen","Große Kurrende Mädchen");
                ensembles.stream().forEach((ensemble)->{
                    Assert.assertNotNull(ensemble);
                    Assert.assertTrue(names.contains(ensemble.getName()));
                    Assert.assertEquals(EnsembleCategory.SINGEN, ensemble.getEnsembleCategory());
                });
            } 
        
        });
    }
}
