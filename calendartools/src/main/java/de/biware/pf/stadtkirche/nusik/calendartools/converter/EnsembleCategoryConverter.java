/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.converter;

import com.ebay.xcelite.converters.ColumnValueConverter;
import de.biware.pf.stadtkirche.nusik.calendartools.EnsembleCategory;

/**
 *
 * @author svenina
 */
public class EnsembleCategoryConverter implements ColumnValueConverter<EnsembleCategory, String> {

    @Override
    public EnsembleCategory serialize(String v) {
        return EnsembleCategory.valueOf(v.toUpperCase());
    }

    @Override
    public String deserialize(EnsembleCategory t) {
        return t.name();
    }
    
}
