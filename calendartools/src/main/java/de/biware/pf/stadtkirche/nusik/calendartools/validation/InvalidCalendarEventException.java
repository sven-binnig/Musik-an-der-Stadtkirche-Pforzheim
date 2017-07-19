/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sbg
 */
@Deprecated
public class InvalidCalendarEventException extends Exception {

    private final List<ViolationSeverity> severities = new ArrayList<>();
    private final StringBuilder message = new StringBuilder();

    public InvalidCalendarEventException() {
    }

    public InvalidCalendarEventException(String string) {
        super(string);
    }

    public InvalidCalendarEventException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public InvalidCalendarEventException(Throwable thrwbl) {
        super(thrwbl);
    }

    public InvalidCalendarEventException(String string, Throwable thrwbl, boolean bln, boolean bln1) {
        super(string, thrwbl, bln, bln1);
    }

    public void addViolationSeverity(ViolationSeverity s) {
        this.severities.add(s);
    }
    
    public boolean isFatal() {
        if (severities.stream().anyMatch((s) -> (s.compareTo(ViolationSeverity.ABBRUCH) == 0))) {
            return true;
        }
        return false;
    }

    @Override
    public String getMessage() {
        return this.message.toString();
    }
    
    public void appendMessage(String m) {
        this.message.append(m);
    }
}
