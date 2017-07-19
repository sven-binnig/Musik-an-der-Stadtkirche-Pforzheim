/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation;

import de.biware.pf.stadtkirche.nusik.calendartools.CalendarEvent;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.result.ValidationFailure;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.result.ViolationResult;
import java.lang.annotation.Annotation;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 *
 * @author sbg
 */
public class Jsr303CalendarEventValidator implements CalendarEventValidator {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ViolationSeverityDetector violationSeverityDetector = ViolationSeverityDetectorCreator.createViolationSeverityDetector();

    //@Override
    public void _validate(CalendarEvent event) throws InvalidCalendarEventException {

        Set<ConstraintViolation<CalendarEvent>> constraintViolations = validator.validate(event);
        String message = "";
        InvalidCalendarEventException exception = new InvalidCalendarEventException();

        for (ConstraintViolation<CalendarEvent> violation : constraintViolations) {
            message += violation.getPropertyPath() + " " + violation.getMessage() + "\r\n";
            Annotation causingAnnotatioin = violation.getConstraintDescriptor().getAnnotation();
            String path = violation.getPropertyPath().toString();
            
            //System.out.println(path + "@" + causingAnnotatioin.annotationType().getName());
            ViolationSeverity s = violationSeverityDetector.detect(path, causingAnnotatioin.annotationType().getName());
            exception.addViolationSeverity(s);
        }

        if (!constraintViolations.isEmpty()) {
            message += event.toString() + "\n\n";
            exception.appendMessage(message);
            throw exception;
        }

    }

    @Override
    public ViolationResult validate(CalendarEvent event) {
        ViolationResult violationResult = new ViolationResult(event);
        Set<ConstraintViolation<CalendarEvent>> constraintViolations = validator.validate(event);
        
        constraintViolations.forEach((violation) -> {
            String message = "in Zeile " + event.getId() + ": " + violation.getPropertyPath() + " " + violation.getMessage() + " => " + event;
            Annotation causingAnnotatioin = violation.getConstraintDescriptor().getAnnotation();
            String path = violation.getPropertyPath().toString();
            ViolationSeverity s = violationSeverityDetector.detect(path, causingAnnotatioin.annotationType().getName());
            violationResult.getFailures().add(new ValidationFailure(path, causingAnnotatioin.annotationType().getName(), s, message, event.getId()));
        });
        return violationResult;
    }

}
