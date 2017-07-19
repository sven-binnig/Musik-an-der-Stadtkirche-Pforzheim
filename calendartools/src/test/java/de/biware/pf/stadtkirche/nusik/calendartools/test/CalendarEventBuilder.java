/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.test;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author svenina
 */
public class CalendarEventBuilder {
    private CalendarEvent calendarEvent;
    
    public CalendarEventBuilder start() {
        this.calendarEvent = new CalendarEvent();
        return this;
    }
    
    public CalendarEventBuilder withTerminart(String art) {
        this.calendarEvent.setArt(art);
        return this;
    }
    
    public CalendarEventBuilder withBeginndatum(Date date) {
        this.calendarEvent.setBeginnDatum(date);
        return this;
    }
    
    public CalendarEventBuilder withBeginnUhrzeit(String zeit) {
        this.calendarEvent.setBeginnUhrzeit(zeit);
        return this;
    }
    
    public CalendarEventBuilder withBeschreibung(String beschreibung) {
        this.calendarEvent.setBeschreibung(beschreibung);
        return this;
    }
    
    public CalendarEventBuilder withEndedatum(Date date) {
        this.calendarEvent.setEndeDatum(date);
        return this;
    }
    
    public CalendarEventBuilder withEndeUhrzeit(String zeit) {
        this.calendarEvent.setEndeUhrzeit(zeit);
        return this;
    }
    
    public CalendarEventBuilder withId(int id) {
        this.calendarEvent.setId(id);
        return this;
    }
    
    public CalendarEventBuilder withOrganisationsuebersicht(String value) {
        this.calendarEvent.setOrganistationsUebersicht(value);
        return this;
    }
    
    public CalendarEventBuilder withOrt(String ort) {
        this.calendarEvent.setOrt(ort);
        return this;
    }
    
    public CalendarEventBuilder withWochentag(String wochentag) {
        this.calendarEvent.setWochentag(wochentag);
        return this;
    }
    
    public CalendarEventBuilder withDynamicColumnValues(Map<String, Object> values) {
        this.calendarEvent.getDynamicCols().putAll(values);
        return this;
    }
    
    public CalendarEvent build() {
        return this.calendarEvent;
    }
}
