/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools.reader;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.reader.SheetReader;
import com.ebay.xcelite.sheet.XceliteSheet;
import de.biware.pf.stadtkirche.nusik.calendartools.ConfigurableEnsemble;
import de.biware.pf.stadtkirche.nusik.calendartools.Ensemble;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author svenina
 */
public class ExcelEnsembleDetectionFactory implements EnsembleDetectionFactory {
    
    private final File excelFile;
    private final String sheetName;
    private final Map<String, Ensemble> excelNameEnsembleMap = new HashMap<>();

    public ExcelEnsembleDetectionFactory(File excelFile, String sheetName) {
        this.excelFile = excelFile;
        this.sheetName = sheetName;
        this.init();
    }
    
    private void init() {
        Xcelite xcelite = new Xcelite(this.excelFile);
        XceliteSheet sheet = xcelite.getSheet(this.sheetName);
        SheetReader<ConfigurableEnsemble> reader = sheet.getBeanReader(ConfigurableEnsemble.class);
        reader.skipHeaderRow(true);
        
        reader.read().forEach(ensemble -> {
            this.excelNameEnsembleMap.put(ensemble.getExcelMappingName(), ensemble);
        });
    }
    

    @Override
    public Ensemble fromExcelName(String execelName) {
        return this.excelNameEnsembleMap.get(execelName);
    }
    
}
