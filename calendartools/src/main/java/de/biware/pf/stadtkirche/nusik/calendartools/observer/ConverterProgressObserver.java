/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.observer;

/**
 *
 * @author sbg
 */
public interface ConverterProgressObserver {
    void setCompleteCount(int count);
    void onBeforeConvert();
    void onAfterContert();
}
