/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.reader;

import de.biware.pf.stadtkirche.nusik.calendartools.Ensemble;

/**
 *
 * @author svenina
 */
public interface EnsembleDetectionFactory {
    //void from(File excelFile, String sheetName);
    Ensemble fromExcelName(String execelName);
}
