/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

import com.ebay.xcelite.exceptions.XceliteException;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.EnsembleDetectionFactory;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.ExcelEnsembleDetectionFactory;
import java.io.File;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author svenina
 */
public class EnsembleReaderTest {
    @Test
    public void ensemblesCanBeReadFromExcel() {
        File excel = new File("src/test/resources/ensembles.xlsx");
        EnsembleDetectionFactory edf = new ExcelEnsembleDetectionFactory(excel, "Tabelle1");
        Ensemble e = edf.fromExcelName("bo");
        Assert.assertTrue(e.getEnsembleCategory() == EnsembleCategory.SPIELEN);
        Assert.assertEquals("Bachorchester", e.getName());
    }
    
    @Test(expected = XceliteException.class)
    public void missingEnsembleConfigurationWillThrowException() {
        File excel = new File("src/test/resources/ensembles.xlsx");
        EnsembleDetectionFactory edf = new ExcelEnsembleDetectionFactory(excel, "murks");
    }
    
}
