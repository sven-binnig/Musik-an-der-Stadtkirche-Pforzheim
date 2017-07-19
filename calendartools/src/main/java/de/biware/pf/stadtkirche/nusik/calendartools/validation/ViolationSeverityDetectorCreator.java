/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author svenina
 */
public class ViolationSeverityDetectorCreator {
    public static ViolationSeverityDetector createViolationSeverityDetector() {
        InputStream source = null;
        try {
            source = getFromEnvironment();
        } catch (Exception ex) {
            try {
                source = getFromSystemProperty();
            } catch (Exception ex1) {
                source = getFromClasspath();
            }
        }
        if(source == null) {
            throw new IllegalStateException("Es konnte keine Quelle für einen ViolationSeverityDetector gefunden werden.");
        }
        
        try {
            ViolationSeverityDetector d = new ConfigurableViolationSeverityDetector(fromSource(source));
            return d;
        } catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }
    
    private static InputStream getFromClasspath() {
        return ViolationSeverityDetectorCreator.class.getResourceAsStream("violationseverity.conf");
    }
    
    private static InputStream getFromEnvironment() throws IOException {
        String filePath = System.getProperty("violationseverity.conf.file");
        return new FileInputStream(filePath);
    }
    
    private static InputStream getFromSystemProperty() throws FileNotFoundException {
        String filePath = System.getenv("violationseverity.conf.file");
        return new FileInputStream(filePath);
    }
    
    private static Map<String, ViolationSeverity> fromSource(InputStream is) throws IOException {
        Map<String, ViolationSeverity> map = new HashMap<>();
        Properties p = new Properties();
        p.load(is);
        //-- convert to map
        p.keySet().stream().forEach((key) -> {
            String value = p.getProperty(key.toString());
            if("pruefen".equalsIgnoreCase(value)) {
                map.put(key.toString(), ViolationSeverity.PRUEFEN);
            } else if("abbruch".equalsIgnoreCase(value)) {
                map.put(key.toString(), ViolationSeverity.ABBRUCH);
            }
        });
        return map;
    }
}
