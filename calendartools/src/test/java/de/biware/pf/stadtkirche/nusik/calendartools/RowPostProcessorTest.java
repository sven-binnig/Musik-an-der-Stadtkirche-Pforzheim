/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

import com.ebay.xcelite.reader.RowPostProcessor;
import de.biware.pf.stadtkirche.nusik.calendartools.test.CalendarEventBuilder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author svenina
 */
public class RowPostProcessorTest {

    private final CalendarEventBuilder builder = new CalendarEventBuilder();

    @Test
    public void doppelpunktInDerUhrzeitWirdKorrigiert() {
        // arrange
        CalendarEvent event
                = builder.start()
                        .withBeginnUhrzeit("11:11") // muss korrigiert werden
                        .withBeginndatum(new Date())
                        .withBeschreibung("Bechreibung")
                        .withEndeUhrzeit("22.22")
                        .withEndedatum(new Date())
                        .withId(0)
                        .withOrt("Hier")
                        .withTerminart("Probe")
                        .withWochentag("Mo")
                        .build();
        // perform
        RowPostProcessor<CalendarEvent> rpp = new CalendarEventRowPostProcessor(null);
        rpp.process(event);
        
        //assert
        Assert.assertFalse(event.getBeginnUhrzeit().contains(":"));
    }
    
    @Test
    public void csvDelimiterInBeschreibungWirdKorrigiert() {
        // arrange
        CalendarEvent event
                = builder.start()
                        .withBeginnUhrzeit("11.11") // muss korrigiert werden
                        .withBeginndatum(new Date())
                        .withBeschreibung("Bechreib,ung")
                        .withEndeUhrzeit("22.22")
                        .withEndedatum(new Date())
                        .withId(0)
                        .withOrt("Hier")
                        .withTerminart("Probe")
                        .withWochentag("Mo")
                        .build();
        // perform
        RowPostProcessor<CalendarEvent> rpp = new CalendarEventRowPostProcessor(null);
        rpp.process(event);
        
        // assert
        Assert.assertFalse(event.getBeschreibung().contains(","));
    }
    
    @Test
    public void groberEnsembleMappingTest() {
        // arrange
        Map<String, Object> ensembleMap = new HashMap<>();
        ensembleMap.put("Motettenchor", "x");
        //ensembleMap.put("Oratorienchor", "x");
        
        CalendarEvent event
                = builder.start()
                        .withBeginnUhrzeit("11:11") // muss korrigiert werden
                        .withBeginndatum(new Date())
                        .withBeschreibung("Bechreibung")
                        .withEndeUhrzeit("22.22")
                        .withEndedatum(new Date())
                        .withId(0)
                        .withOrt("Hier")
                        .withTerminart("Probe")
                        .withWochentag("Mo")
                        .withDynamicColumnValues(ensembleMap)
                        .build();
        // perform
        RowPostProcessor<CalendarEvent> rpp = new CalendarEventRowPostProcessor(null);
        rpp.process(event);
        
        // assert
        Assert.assertTrue(event.getEnsembles().get(0).getName().equals("Motettenchor"));
    }
}
