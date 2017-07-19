/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author sbg
 */
public class NoCSVDelimiterValidator implements ConstraintValidator<NoCSVDelimiter, String> {

    @Override
    public void initialize(NoCSVDelimiter a) {
        // nop
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext cvc) {
        //return t != null && !t.contains(",");
        if(t != null) {
            return !t.contains(",");
        }
        return true;
    }
    
}
