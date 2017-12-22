/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.biware.pf.stadtkirche.nusik.calendartools;

import com.ebay.xcelite.annotations.Column;
import de.biware.pf.stadtkirche.nusik.calendartools.validation.NoCSVDelimiter;

/**
 *
 * @author svenina
 */
public class ConfigurableEnsemble implements Ensemble {
    @Column(name = "klarname")
    @NoCSVDelimiter
    private final String klarname;
    @Column(name = "excelName")
    @NoCSVDelimiter
    private final String excelMappingName;
    @Column(name = "cmsName")
    @NoCSVDelimiter
    private final String cmsMappingName;
    @Column(name = "ensembleCategory")
    @NoCSVDelimiter
    private final EnsembleCategory ensembleCategory;

    public ConfigurableEnsemble(String klarname, String excelMappingName, String cmsMappingName, EnsembleCategory ensembleCategory) {
        this.klarname = klarname;
        this.excelMappingName = excelMappingName;
        this.cmsMappingName = cmsMappingName;
        this.ensembleCategory = ensembleCategory;
    }
    
    public ConfigurableEnsemble(String klarname, String excelMappingName, String cmsMappingName, String ensembleCategoryName) {
        this.klarname = klarname;
        this.excelMappingName = excelMappingName;
        this.cmsMappingName = cmsMappingName;
        this.ensembleCategory = EnsembleCategory.valueOf(ensembleCategoryName.toUpperCase());
    }
    
    

    @Override
    public String getName() {
        return this.klarname;
    }

    @Override
    public EnsembleCategory getEnsembleCategory() {
        return this.ensembleCategory;
    }

    @Override
    public String getExcelMappingName() {
        return this.excelMappingName;
    }

    @Override
    public String getCMSMappingName() {
        return this.cmsMappingName;
    }
    
}
