/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation;

/**
 *
 * @author svenina
 */
public interface ViolationSeverityDetector {
    ViolationSeverity detect(String propertyPath, String annotationRule);
}
