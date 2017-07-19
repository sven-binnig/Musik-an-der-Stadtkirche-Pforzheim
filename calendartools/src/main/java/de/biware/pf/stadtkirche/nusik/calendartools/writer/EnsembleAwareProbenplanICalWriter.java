/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.writer;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.Ensemble;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

/**
 *
 * @author sbg
 */
public class EnsembleAwareProbenplanICalWriter extends AbstractEnsembleAwareProbenplanWriter {

    private File directory;
    private final Map<String, ICalCoordinates> ensembleMap = new HashMap<>();
    private final UidGenerator ug;

    public EnsembleAwareProbenplanICalWriter() {
        try {
            ug = new UidGenerator("uidGen");
        } catch (SocketException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    

    @Override
    public void doFor(CalendarEvent event, Ensemble ensemble) {
        try {
            ICalCoordinates coordinate = forEnsemble(ensemble);
            DateTime dtStart = fromCalendarEventParts(event.getBeginnDatum(), event.getBeginnUhrzeit());
            DateTime dtEnde = fromCalendarEventParts(event.getEndeDatum(), event.getEndeUhrzeit());
            VEvent calEvent = new VEvent(dtStart, dtEnde, ensemble.getName() + ": " + event.getBeschreibung());
            //calEvent.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.TIME);
            // TODO initialise as an all-day event.
            //christmas.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
            // TODO generiere Uid direkt als String mit UUID.randomUUID().toString()
            Uid uid = ug.generateUid();
            calEvent.getProperties().add(uid);
            // categories: termin-art && ensemble
            Categories cArt = new Categories(event.getArt());
            Categories cEnsemble = new Categories(ensemble.getName());
            calEvent.getProperties().add(cArt);
            calEvent.getProperties().add(cEnsemble);
            
            coordinate.getiCalendar().getComponents().add(calEvent);

        } catch (FileNotFoundException  ex) {
            Logger.getLogger(EnsembleAwareProbenplanICalWriter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private DateTime fromCalendarEventParts(java.util.Date datum, String uhrzeit) {
        java.util.Calendar date = new GregorianCalendar();
        date.setTime(datum);
        // Uhrzeit:
        //print(uhrzeit);
        if (uhrzeit != null) {
            int iDot = uhrzeit.indexOf(':');
            if(iDot == -1) {
                iDot = uhrzeit.indexOf('.');
            }
            if (iDot != -1) {
                date.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(uhrzeit.substring(0, iDot)));
                date.set(java.util.Calendar.MINUTE, Integer.parseInt(uhrzeit.substring(iDot + 1)));
            } else {
                print("Formatierungsfehler bei Uhrzeit '" + uhrzeit + "'");
            }
        }
        return new DateTime(date.getTime());
    }

    @Override
    public void useDirectory(File directory) {
        this.directory = directory;
    }

    @Override
    public void close() {
        for (ICalCoordinates coordinates : this.ensembleMap.values()) {
            try {
                CalendarOutputter outputter = new CalendarOutputter();
                outputter.output(coordinates.getiCalendar(), coordinates.getOutputStream());

            } catch (IOException | ValidationException ex) {
                Logger.getLogger(EnsembleAwareProbenplanICalWriter.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void print(String msg) {
        //System.out.println("[" + getClass().getSimpleName() + "] " + msg);
        super.fireReaderWriterMessage(getClass().getSimpleName(), msg);
    }

    private ICalCoordinates forEnsemble(Ensemble ensemble) throws FileNotFoundException {
        ICalCoordinates coordinates = this.ensembleMap.get(ensemble.getName());
        if (coordinates != null) {
            return coordinates;
        }
        print(" -- neues Dokument für " + ensemble.getName());
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        FileOutputStream writer = new FileOutputStream(new File(this.directory, ensemble.getName() + ".ics"));
        coordinates = new ICalCoordinates(writer, calendar);
        this.ensembleMap.put(ensemble.getName(), coordinates);
        return coordinates;

    }

    //----------------------------------------
    private class ICalCoordinates {

        private final FileOutputStream outputStream;
        private final Calendar iCalendar;

        public ICalCoordinates(FileOutputStream outputStream, Calendar iCalendar) {
            this.outputStream = outputStream;
            this.iCalendar = iCalendar;
        }

        public FileOutputStream getOutputStream() {
            return outputStream;
        }

        public Calendar getiCalendar() {
            return iCalendar;
        }

    }

}
