/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

import com.ebay.xcelite.reader.RowPostProcessor;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.CalendarEventExcelReader;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.EnsembleDetectionFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author svenina
 */
public class CalendarEventRowPostProcessor implements RowPostProcessor<CalendarEvent> {

    /*private static final Map<String, Class<? extends Ensemble>> classMap = new HashMap<>();

    static {
        classMap.put("Oratorienchor", Oratorienchor.class);
        classMap.put("Motettenchor", Motettenchor.class);
        classMap.put("Bachorchester", Bachorchester.class);
        classMap.put("Große Kurrende Jungen", GrosseKurrendeJungen.class);
        classMap.put("Große Kurrende Mädchen", GrosseKurrendeMaedchen.class);
        classMap.put("Jugendkantorei", Jugendkantorei.class);
        classMap.put("Kantate zum Mitsingen", KantateZumMitsingen.class);
        classMap.put("Kleine Kurrende", KleineKurrende.class);
        classMap.put("Projektkantorei", Projektkantorei.class);
        classMap.put("Singschulklasse I/II", Singschulklasse.class);
        classMap.put("Bläserensemble", Blaeserensemble.class);
        classMap.put("Hebel AG Stimmbildung Klasse 5", Hebel5.class);
        classMap.put("Unterstufenchor Hebel", HebelUnterstufenchor.class);
        classMap.put("Singen im Kindergarten", SingenImKindergarten.class);
    }*/
    private int row = 0;
    private final CalendarEventExcelReader calendarEventExcelReader;
    private final EnsembleDetectionFactory ensembleDetector;

    public CalendarEventRowPostProcessor(CalendarEventExcelReader calendarEventExcelReader, EnsembleDetectionFactory ensembleDetector) {
        this.calendarEventExcelReader = calendarEventExcelReader;
        this.ensembleDetector = ensembleDetector;
    }

    /**
     * Abschlussarbeiten an einem CalendarEvent.
     *
     * @param t CalendarEvent
     * @return true
     */
    @Override
    public boolean process(CalendarEvent t) {
        ++row;
        if (t.getDynamicCols() != null) {

            // avoid auto generated lambda here ...
            for(String key: t.getDynamicCols().keySet()) {
                Ensemble ensemble = this.ensembleDetector.fromExcelName(key);
                t.getEnsembles().add(ensemble);
            }
        }
        if (t.getArt() == null) {
            t.setArt("Probe");
        }
        this.replaceCharacterInUhrzeit(':', '.', t);
        t.setEndeUhrzeit(this.convertUhrzeitIfNeccessary(t.getEndeUhrzeit()));
        t.setBeginnUhrzeit(this.convertUhrzeitIfNeccessary(t.getBeginnUhrzeit()));
        t.setId(row);
        this.replaceCSVDelimiters(t);

        return true;
    }

    private void replaceCSVDelimiters(CalendarEvent t) {
        if (t.getBeschreibung() != null) {
            t.setBeschreibung(t.getBeschreibung().replace(",", " -"));
        }
    }

    private void replaceCharacterInUhrzeit(char toReplace, char replaceWith, CalendarEvent t) {
        if (t.getBeginnUhrzeit() != null) {
            t.setBeginnUhrzeit(t.getBeginnUhrzeit().replace(toReplace, replaceWith));
        }
        if (t.getEndeUhrzeit() != null) {
            t.setEndeUhrzeit(t.getEndeUhrzeit().replace(toReplace, replaceWith));
        }
    }

    private String convertUhrzeitIfNeccessary(String uhrzeit) {
        if (uhrzeit != null && uhrzeit.length() > 5 && uhrzeit.charAt(4) == '.') {
            String neu = uhrzeit.substring(0, 2) + ":" + uhrzeit.substring(2, 4);
            return neu;
        } else if (uhrzeit != null && uhrzeit.length() > 4 && uhrzeit.charAt(3) == '.') {
            // also vor 10.00
            String neu = uhrzeit.substring(0, 1) + ":" + uhrzeit.substring(1, 3);
            return neu;
        } else if (uhrzeit != null && uhrzeit.contains("Uhr") && uhrzeit.contains(".")) {
            int posUhr = uhrzeit.indexOf("Uhr");
            int posDot = uhrzeit.indexOf(".");
            String tmp = uhrzeit.substring(0, posUhr).trim();
            String neu = tmp.substring(0, posDot);
            neu += ":" + tmp.substring(posDot + 1);
            return neu;
        }
        return uhrzeit;
    }

}
