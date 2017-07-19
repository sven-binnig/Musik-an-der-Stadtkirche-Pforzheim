/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation;

import java.util.Map;

/**
 *
 * @author svenina
 */
public class ConfigurableViolationSeverityDetector implements ViolationSeverityDetector {
    private final Map<String, ViolationSeverity> severityMap;

    public ConfigurableViolationSeverityDetector(Map<String, ViolationSeverity> severityMap) {
        this.severityMap = severityMap;
    }
    
    
    @Override
    public ViolationSeverity detect(String propertyPath, String annotationRule) {
        String key = propertyPath + "." + annotationRule;
        ViolationSeverity severity = this.severityMap.get(key);
        return severity != null ? severity : ViolationSeverity.PRUEFEN;
    }
    
}
