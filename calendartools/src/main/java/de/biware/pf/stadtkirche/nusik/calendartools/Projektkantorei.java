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
public class Projektkantorei implements Ensemble {

    @Override
    public String getName() {
        return "Projektkantorei";
    }

    @Override
    public EnsembleCategory getEnsembleCategory() {
        return EnsembleCategory.SINGEN;
    }
    
}
