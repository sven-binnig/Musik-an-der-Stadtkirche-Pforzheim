/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.test;

import de.biware.pf.stadtkirche.nusik.calendartools.ConfigurableEnsemble;
import de.biware.pf.stadtkirche.nusik.calendartools.Ensemble;
import de.biware.pf.stadtkirche.nusik.calendartools.EnsembleCategory;
import de.biware.pf.stadtkirche.nusik.calendartools.reader.EnsembleDetectionFactory;

/**
 *
 * @author svenina
 */
public class NameBasedEnsembleDetectionFactory implements EnsembleDetectionFactory {

    @Override
    public Ensemble fromExcelName(String execelName) {
        return new ConfigurableEnsemble(execelName, execelName, execelName, EnsembleCategory.SPIELEN);
    }
    
}
