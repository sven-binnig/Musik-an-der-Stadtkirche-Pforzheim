/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.musik.calendartools.ui.service;

/**
 *
 * @author svenina
 */
public class ConvertException extends Exception {

    public ConvertException() {
    }

    public ConvertException(String string) {
        super(string);
    }

    public ConvertException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public ConvertException(Throwable thrwbl) {
        super(thrwbl);
    }

    public ConvertException(String string, Throwable thrwbl, boolean bln, boolean bln1) {
        super(string, thrwbl, bln, bln1);
    }
    
}
