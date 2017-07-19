/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.test;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 *
 * @author svenina
 */
public class ListOfCalendarEventsMatcher extends TypeSafeMatcher<Collection<CalendarEvent>> {

    private final Collection<CalendarEvent> events;

    public ListOfCalendarEventsMatcher(Collection<CalendarEvent> events) {
        this.events = events;
    }

    @Override
    protected boolean matchesSafely(Collection<CalendarEvent> t) {
        Map<Integer, CalendarEvent> own = events.stream().collect(Collectors.toMap(CalendarEvent::getId, item -> item));
        Map<Integer, CalendarEvent> forein = t.stream().collect(Collectors.toMap(CalendarEvent::getId, item -> item));
        if (t.size() == events.size()) {
            own.entrySet().forEach((entryOwn) -> {
                CalendarEvent entryForeign = forein.get(entryOwn.getValue().getId());
                if (!entryOwn.getValue().equals(entryForeign)) {
                    if (!entryOwn.getValue().getDynamicCols().isEmpty() && entryForeign.getEnsembles().size() > 0) {
                        // ignore 'cause it's ok
                    } else {
                        throw new AssertionError("Die CalenardEvents mit der ID "
                                + entryOwn.getValue().getId() + " sind nicht gleich:" + entryOwn.getValue() + " => " + entryForeign);
                    }
                }
            });
        } else {
            throw new AssertionError("Die Liste mit CalendarEvent ist unterschiedlich gross");
        }

        return true;
    }

    @Override
    public void describeTo(Description d) {
        d.appendText("matching collection of calendar events");
    }

    public static ListOfCalendarEventsMatcher matches(Collection<CalendarEvent> preparedEvents) {
        return new ListOfCalendarEventsMatcher(preparedEvents);
    }
}
