/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.writer;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.Ensemble;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author svenina
 */
public abstract class AbstractEnsembleAwareProbenplanWriter extends AbstractCalendarEventWriter {

    public abstract void doFor(CalendarEvent event, Ensemble ensemble);

    @Override
    public void write(Collection<CalendarEvent> events) {
        print("Wandle " + events.size() + " Termine in Probenpläne um.");
        fireMaximumCount(events.size());
        for (CalendarEvent event : events) {
            fireOnBeforeConvert();
            for (Ensemble ensemble : event.getEnsembles()) {
                doFor(event, ensemble);
            }
            fireOnAfterConvert();
        }
    }

    protected int getCellsPerRow() {
        return 4;
    }

    protected List<String> getCelldataOfRow(CalendarEvent event, SimpleDateFormat dateFormatter) {
        List<String> cellData = new ArrayList<>();

        cellData.add(event.getWochentag());
        if (event.getBeginnDatum().equals(event.getEndeDatum())) {
            StringBuilder value = new StringBuilder(dateFormatter.format(event.getBeginnDatum()));
            // ist keine Uhrzeit hinterlegt, wird keine angezeigt.
            if (event.getBeginnUhrzeit() != null && event.getEndeUhrzeit() != null) {
                value.append(" ").append(event.getBeginnUhrzeit()).append(" - ").append(event.getEndeUhrzeit());
            }
            cellData.add(value.toString());
        } else {
            StringBuilder value = new StringBuilder(dateFormatter.format(event.getBeginnDatum()));
            value.append(" - ").append(dateFormatter.format(event.getEndeDatum()));
            cellData.add(value.toString());
        }

        cellData.add(event.getBeschreibung());
        cellData.add(event.getOrt());

        return cellData;
    }

    protected String formatUhrzeitConformingToStadtkircheCI(String uhrzeit) {
        if (uhrzeit == null) {
            return uhrzeit;
        }

        String formatted = uhrzeit.replace(":", ".");
        // keine führende Null
        if (formatted.charAt(0) == '0') {
            return formatted.substring(1);
        }
        return formatted;
    }

    private void print(String msg) {
        //System.out.println("[" + getClass().getSimpleName() + "] " + msg);
        super.fireReaderWriterMessage(getClass().getSimpleName(), msg);
    }
}
