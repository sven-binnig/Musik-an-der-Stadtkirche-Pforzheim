/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

import com.ebay.xcelite.annotations.AnyColumn;
import com.ebay.xcelite.annotations.Column;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.NoCSVDelimiter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Domainobjekt CalendarEvent. JSR 303 Validierbar vgl
 * http://openbook.rheinwerk-verlag.de/java7/1507_17_001.html
 *
 * @author svenina
 */
public class CalendarEvent {

    // "Wochen-tag"	
    @Column(name = "Wochentag")
    @NoCSVDelimiter
    private String wochentag;
    // "Beginn-datum"	
    @Column(name = "Beginndatum", dataFormat = "TT.MM.JJJJ")
    @NotNull
    //@Future
    private Date beginnDatum;
    // "Beginn-uhrzeit"	
    @Column(name = "Beginnuhrzeit")
    @NoCSVDelimiter
    private String beginnUhrzeit;
    // "Ende-datum"	
    @Column(name = "Endedatum", dataFormat = "TT.MM.JJJJ")
    @NotNull
    //@Future
    private Date endeDatum;
    // "Ende-uhrzeit"
    @Column(name = "Endeuhrzeit")
    @NoCSVDelimiter
    private String endeUhrzeit;
    // Beschreibung	
    @Column(name = "Beschreibung")
    @NotNull
    @NoCSVDelimiter
    private String beschreibung;
    // Ort	
    @Column(name = "Ort")
    @NoCSVDelimiter
    private String ort;
    // Art	
    @Column(name = "Art")
    @NoCSVDelimiter
    private String art;

    @Column(name = "Organisationsübersicht")
    private String organistationsUebersicht;

    @Size(min = 1)
    private final List<Ensemble> ensembles = new ArrayList<>();

    @AnyColumn
    private Map<String, Object> dynamicCols = new HashMap<>();

    private int id;

    public CalendarEvent() {
        // nop
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CalendarEvent(String wochentag, Date beginnDatum, String beginnUhrzeit, Date endeDatum, String endeUhrzeit, String beschreibung, String ort, String art, String singschuleklasse, String kleineKurrende, String grosseKurrendeJungen, String grosseKurrendeMaedchen, String jugendkantorei, String motettenchor, Ensemble oratorienchor, String projektkantorei, String bachorchester, String kantateZumMitsingen, String organistationsUebersicht) {
        this.wochentag = wochentag;
        this.beginnDatum = beginnDatum;
        this.beginnUhrzeit = beginnUhrzeit;
        this.endeDatum = endeDatum;
        this.endeUhrzeit = endeUhrzeit;
        this.beschreibung = beschreibung;
        this.ort = ort;
        this.art = art;
        this.organistationsUebersicht = organistationsUebersicht;
    }

    public void setWochentag(String wochentag) {
        this.wochentag = wochentag;
    }

    public void setBeginnDatum(Date beginnDatum) {
        this.beginnDatum = beginnDatum;
    }

    public void setBeginnUhrzeit(String beginnUhrzeit) {
        this.beginnUhrzeit = beginnUhrzeit;
    }

    public void setEndeDatum(Date endeDatum) {
        this.endeDatum = endeDatum;
    }

    public void setEndeUhrzeit(String endeUhrzeit) {
        this.endeUhrzeit = endeUhrzeit;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public void setOrganistationsUebersicht(String organistationsUebersicht) {
        this.organistationsUebersicht = organistationsUebersicht;
    }

    public String getWochentag() {
        return wochentag;
    }

    public Date getBeginnDatum() {
        return beginnDatum;
    }

    public String getBeginnUhrzeit() {
        return beginnUhrzeit;
    }

    public Date getEndeDatum() {
        return endeDatum;
    }

    public String getEndeUhrzeit() {
        return endeUhrzeit;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public String getOrt() {
        return ort;
    }

    public String getArt() {
        return art;
    }

    public String getOrganistationsUebersicht() {
        return organistationsUebersicht;
    }

    public List<Ensemble> getEnsembles() {
        return ensembles;
    }

    public Map<String, Object> getDynamicCols() {
        return dynamicCols;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" + "wochentag=" + wochentag + ", beginnDatum=" + beginnDatum + ", beginnUhrzeit=" + beginnUhrzeit + ", endeDatum=" + endeDatum + ", endeUhrzeit=" + endeUhrzeit + ", beschreibung=" + beschreibung + ", ort=" + ort + ", art=" + art + ", organistationsUebersicht=" + organistationsUebersicht + ", ensembles=" + ensembles + ", dynamicCols=" + dynamicCols + '}';
    }

    @AssertTrue(message = "Endedatum darf nicht vor dem Beginndatum liegen.")
    public boolean isEndedatumNachBeginndatum() {
        //if (endeDatum.getTime() >= beginnDatum.getTime()) {
        //    return true;
        //}
        if (endeDatum != null && beginnDatum != null && beginnUhrzeit != null && endeUhrzeit != null) {
            SimpleDateFormat formatter_1 = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat formatter_2 = new SimpleDateFormat("dd.MM.yyyy HH:mm");

            try {
                Date von = formatter_2.parse(formatter_1.format(beginnDatum) + " " + beginnUhrzeit);
                Date bis = formatter_2.parse(formatter_1.format(endeDatum) + " " + endeUhrzeit);
                return bis.getTime() >= von.getTime();
            } catch (ParseException ex) {
            }
            return false;
        } else if (endeDatum != null && beginnDatum != null) {
            return endeDatum.getTime() >= beginnDatum.getTime();
        }
        return false;
    }

}
