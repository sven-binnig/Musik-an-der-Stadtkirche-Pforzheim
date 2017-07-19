/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

import de.biware.pf.stadtkirche.nusik.calendartools.reader.CalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.DefaultCalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.test.CalendarEventBuilder;
import de.biware.pf.stadtkirche.nusik.calendartools.test.ExcelFileCreator;
import de.biware.pf.stadtkirche.nusik.calendartools.test.ListOfCalendarEventsMatcher;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author svenina
 */
public class DynamicExcelReaderTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final CalendarEventBuilder builder = new CalendarEventBuilder();
    
    private static final String SHEET = "Tabelle1";

    @Test
    public void execelReaderReadsAllEntriesInExcel() {
        // arrange: 100 items
        Collection<CalendarEvent> events = new ArrayList<>();
        Map<String, Object> ensembleMap = new HashMap<>();
        ensembleMap.put("Motettenchor", "x");
        for (int item = 1; item <= 100; ++item) {
            events.add(builder.start()
                    .withBeginnUhrzeit("11.11") 
                    .withBeginndatum(new Date())
                    .withBeschreibung("Bechreibung")
                    .withEndeUhrzeit("22.22")
                    .withEndedatum(new Date())
                    .withId(item)
                    .withOrt("Hier")
                    .withTerminart("Probe")
                    .withWochentag("Mo")
                    .withDynamicColumnValues(ensembleMap)
                    .build());
        }
        
        File xls = new File(this.temporaryFolder.getRoot(), UUID.randomUUID().toString() + ".xls");
        ExcelFileCreator creator = new ExcelFileCreator(xls, SHEET);
        creator.create(events);
        
        CalendarEventExcelReader reader
                = new DefaultCalendarEventExcelReader(xls, SHEET);
        
        Collection<CalendarEvent> readEvents = reader.read();
        
        Assert.assertThat(readEvents, ListOfCalendarEventsMatcher.matches(events));

    }
}
