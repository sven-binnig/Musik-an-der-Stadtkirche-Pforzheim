/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.Motettenchor;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.result.ViolationResult;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author svenina
 */
public class JsrValidatorTest {

    @Test
    public void fehlendesEnsembleIstNichtFatal() {
        CalendarEvent event = new CalendarEvent();
        event.setArt("probe");
        event.setBeginnDatum(new Date());
        event.setBeginnUhrzeit("0:00");
        event.setBeschreibung("Beschreibung");
        event.setEndeDatum(new Date());
        event.setEndeUhrzeit("11:11");
        event.setId(1);
        event.setOrt("sk");
        event.setWochentag("Mo");
        event.getEnsembles().clear(); // invalid

        CalendarEventValidator validator = new Jsr303CalendarEventValidator();
        ViolationResult result = validator.validate(event);
        if (result.isFatal()) {
            Assert.fail("severity ist fatal, obwohl check erwartet wurde");

        }
    }

    @Test
    public void beschreibungMitCSVDelimiterIstFatal() {
        CalendarEvent event = new CalendarEvent();
        event.setArt("probe");
        event.setBeginnDatum(new Date());
        event.setBeginnUhrzeit("0:00");
        event.setBeschreibung("Beschreibu,ng"); // invalid
        event.setEndeDatum(new Date());
        event.setEndeUhrzeit("11:11");
        event.setId(1);
        event.setOrt("sk");
        event.setWochentag("Mo");
        event.getEnsembles().add(new Motettenchor());

        CalendarEventValidator validator = new Jsr303CalendarEventValidator();
        ViolationResult result = validator.validate(event);
        if (!result.isFatal()) {
            Assert.fail("severity ist check, obwohl fatal erwartet wurde");
        }
    }
    
    @Test
    public void endeDatumForBeginnDatumIstFatal() {
        CalendarEvent event = new CalendarEvent();
        event.setArt("probe");
        event.setBeginnDatum(new Date());
        event.setBeginnUhrzeit("0.00");
        event.setBeschreibung("Beschreibung"); 
        event.setEndeDatum(new Date(0)); // invalid
        event.setEndeUhrzeit("11:11");
        event.setId(1);
        event.setOrt("sk");
        event.setWochentag("Mo");
        event.getEnsembles().add(new Motettenchor());

        CalendarEventValidator validator = new Jsr303CalendarEventValidator();
        ViolationResult result = validator.validate(event);
        System.out.println(result.getFailures().get(0).getPropertyPath());
        if (!result.isFatal()) {
            Assert.fail("severity ist check, obwohl fatal erwartet wurde");
        }
    }
    
    @Test
    public void gleichesDatumAberFruehererEndeZeitIstFatal() {
        CalendarEvent event = new CalendarEvent();
        event.setArt("probe");
        event.setBeginnDatum(new Date());
        event.setBeginnUhrzeit("20:00");
        event.setBeschreibung("Beschreibung"); 
        event.setEndeDatum(new Date()); 
        event.setEndeUhrzeit("11:11"); // invalid
        event.setId(1);
        event.setOrt("sk");
        event.setWochentag("Mo");
        event.getEnsembles().add(new Motettenchor());

        CalendarEventValidator validator = new Jsr303CalendarEventValidator();
        ViolationResult result = validator.validate(event);
        System.out.println(result.getFailures().get(0).getPropertyPath());
        if (!result.isFatal()) {
            Assert.fail("severity ist check, obwohl fatal erwartet wurde");
        }
    }

}
