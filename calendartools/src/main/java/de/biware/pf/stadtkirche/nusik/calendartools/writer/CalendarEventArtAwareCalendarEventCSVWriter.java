/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.writer;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.Ensemble;
import de.biware.pf.stadtkirche.nusik.calendartools.EnsembleCategory;
import de.biware.pf.stadtkirche.nusik.calendartools.processor.CalendarEventManipulatingProcessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author svenina
 */
public class CalendarEventArtAwareCalendarEventCSVWriter extends AbstractCalendarEventWriter {

    private File directory;
    private CalendarEventManipulatingProcessor calendarEventProcessor = null;
    private final Map<String, PrintWriter> writerMap = new HashMap<>();
    private final Map<String, Integer> counterMap = new HashMap<>();
    // lt. Mail von Herrn Scheffczyk vom 06.07.2017:
    // Feld für Beschreibung wurde von „summary“ auf „texarea“ umgestellt (Wichtig für den Import per CSV)
    //-- private static final String HEADER_COLS = "title,dateStart,dateEnd,dateWholeDay,headline,summary,place,ensemblesSinging,ensemblesPlaying";
    private static final String HEADER_COLS = "title,dateStart,dateEnd,dateWholeDay,headline,textarea,place,ensemblesSinging,ensemblesPlaying";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat DATE_FORMATTER_TITLE = new SimpleDateFormat("dd.MM.yyyy");
    private static final String DELIMITER = ",";

    public void setCalendarEventProcessor(CalendarEventManipulatingProcessor calendarEventProcessor) {
        this.calendarEventProcessor = calendarEventProcessor;
    }
    
        
    public void print(String msg) {
        //System.out.println("[" + getClass().getSimpleName() + "] " + msg);
        super.fireReaderWriterMessage(getClass().getSimpleName(), msg);
    }
    
    @Override
    public void write(Collection<CalendarEvent> events) {
        int zeile = 0;
        //print("Wandle " + events.size() + " Termine in CSV um.");
        fireMaximumCount(events.size());
        for (CalendarEvent event : events) {
            ++zeile;
            fireOnBeforeConvert();
            
            if(this.calendarEventProcessor != null) {
                print("--> will manipulate event for test purpose: " + event.toString());
                event = this.calendarEventProcessor.process(event);
                print("--> have manipulate event for test purpose: " + event.toString());
            }
            
            String beginnUhrzeit = event.getBeginnUhrzeit() != null ? event.getBeginnUhrzeit() : "";
            String endeUhrzeit = event.getEndeUhrzeit() != null ? event.getEndeUhrzeit() : "";
            String endeDatum;
            if (endeUhrzeit.length() > 0 && event.getEndeDatum() == null) {
                // endeuhrzeit ohne endedatum => endedatum = beginndatum
                endeDatum = DATE_FORMATTER.format(event.getBeginnDatum());
            } else if(endeUhrzeit.isEmpty() && event.getEndeDatum() == null) {
                // kein endedatum und keine endeuhrzeit => beides leer
                endeDatum = "";
            } else {
                endeDatum = DATE_FORMATTER.format(event.getEndeDatum());
            }
            String beginnDatum = DATE_FORMATTER.format(event.getBeginnDatum());
            // keine beginnuhrzeit && keine endeuhrzeit => wholeDay = 1
            String wholeDay = event.getBeginnUhrzeit() == null && event.getEndeUhrzeit() == null ? "1" : "0";
            
            StringBuilder line = new StringBuilder();
            //title
            line.append(DATE_FORMATTER_TITLE.format(event.getBeginnDatum())).append(" ").append(beginnUhrzeit).append(" ").append(event.getArt());
            line.append(DELIMITER);
            // datestart
            line.append(beginnDatum).append(" ").append(beginnUhrzeit);
            line.append(DELIMITER);
            //date end
            line.append(endeDatum).append(" ").append(endeUhrzeit);
            line.append(DELIMITER);
            // TODO erkenne einen ganzen Tag
            line.append(wholeDay); // wholeDay
            line.append(DELIMITER);
            line.append(event.getBeschreibung()); // headline
            line.append(DELIMITER);
            line.append(" "); // leere Beschreibung
            line.append(DELIMITER);
            line.append(event.getOrt() != null ? event.getOrt() : " ");
            line.append(DELIMITER);

            StringBuilder ensembleSinging = new StringBuilder();
            StringBuilder ensemblePlaying = new StringBuilder();
            StringBuilder current = null;
            for (Ensemble ensemble : event.getEnsembles()) {
                if (ensemble.getEnsembleCategory() == EnsembleCategory.SINGEN) {
                    current = ensembleSinging;
                    //ensembleSinging.append(ensemble.getName()).append("|");
                } else if (ensemble.getEnsembleCategory() == EnsembleCategory.SPIELEN) {
                    current = ensemblePlaying;
                    //ensemblePlaying.append(ensemble.getName()).append("|");
                }
                if (current.length() != 0) {
                    current.append("|");
                }
                current.append(ensemble.getName());
            }
            line.append(ensembleSinging);
            line.append(DELIMITER);
            line.append(ensemblePlaying);
            line.append(DELIMITER);
            // get writer by art
            //System.out.println("suche Writer fuer " + event.getArt());
            PrintWriter writer = writerMap.get(event.getArt());
            if (writer == null) {
                writer = writerMap.get("unbekannt");
                print("unbekannte Terminart in Zeile " + event.getId() + " '" + event.getArt() + "' für " + event.toString());
            } else {
                this.incrementForCalendarArtEvent(event.getArt());
            }
            writer.println(line.toString());
            
            fireOnAfterConvert();
        }
        reportCountOfCalendarEventArt(events.size());
    }

    @Override
    public void useDirectory(File directory) {
        if (directory.isDirectory()) {
            this.directory = directory;
            this.prepareWriterMap();
        } else {
            throw new IllegalStateException(directory.getAbsolutePath() + " ist kein Verzeichnis");
        }
    }

    private void prepareWriterMap() {
        this.writerMap.put("Probe", this.forCalendarEventArt("Probe"));
        this.writerMap.put("Konzert", this.forCalendarEventArt("Konzert"));
        this.writerMap.put("Gottesdienst", this.forCalendarEventArt("Gottesdienst"));
        this.writerMap.put("Einführung", this.forCalendarEventArt("Einfuehrung"));
        this.writerMap.put("Musikvermittlung", this.forCalendarEventArt("Musikvermittlung"));
        this.writerMap.put("unbekannt", this.forCalendarEventArt("andere"));
    }

    private PrintWriter forCalendarEventArt(String art) {
        try {
            //PrintWriter pw = new PrintWriter(new File(directory, art + ".csv"));
            PrintWriter pw =
                    new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(directory, art + ".csv")), "UTF-8"));
            pw.println(HEADER_COLS);
            return pw;
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    private void incrementForCalendarArtEvent(String art) {
        Integer count = this.counterMap.get(art);
        if(count == null) {
            this.counterMap.put(art, 1);
        } else {
            count++;
            this.counterMap.put(art, count);
        }
    }
    
    
    private void reportCountOfCalendarEventArt(int complete) {
        this.counterMap.keySet().forEach((art) -> {
            print(" -- " +art + " = " + this.counterMap.get(art));
        });
        print("Wandle " + complete + " Termine in CSV um.");
    }

    @Override
    public void close() {
        for (PrintWriter writer : this.writerMap.values()) {
            writer.flush();
            writer.close();
        }
    }

}
