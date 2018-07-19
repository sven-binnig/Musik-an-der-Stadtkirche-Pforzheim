/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

/**
 *
 * @author svenina
 */
public interface Ensemble {
    public String getName();
    public EnsembleCategory getEnsembleCategory();
    default String getExcelMappingName() {
        return getName();
    }
    default String getCMSMappingName() {
        return getName();
                
    }
}
