/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation.result;

import de.biware.pf.stadtkirche.nusik.calendartools.validation.ViolationSeverity;

/**
 *
 * @author svenina
 */
public class ValidationFailure {

    private final String propertyPath;
    private final String annotation;
    private final ViolationSeverity violationSeverity;
    private final String message;
    private final int line;

    public ValidationFailure(String propertyPath, String annotation, ViolationSeverity violationSeverity, String message, int line) {
        this.propertyPath = propertyPath;
        this.annotation = annotation;
        this.violationSeverity = violationSeverity;
        this.message = message;
        this.line = line;
    }

    public int getLine() {
        return line;
    }
    
    

    public String getPropertyPath() {
        return propertyPath;
    }

    public String getAnnotation() {
        return annotation;
    }

    public ViolationSeverity getViolationSeverity() {
        return violationSeverity;
    }

    public String getMessage() {
        return message;
    }

    

}
