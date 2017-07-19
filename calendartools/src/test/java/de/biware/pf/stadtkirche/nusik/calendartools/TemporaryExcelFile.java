/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.rules.ExternalResource;

/**
 *
 * @author sbg
 */
public class TemporaryExcelFile extends ExternalResource {
    
    public File getDefaultExcelFile() throws URISyntaxException {
        URL url = this.getClass().getClassLoader().getResource("Gesamtprobenplan_II_2016_für_Webkalender.xlsx");
        File xls = new File(new URI(url.toExternalForm()));
        return xls;
        
    }
}
