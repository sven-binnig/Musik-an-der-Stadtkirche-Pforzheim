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
    private String klarname;
    @Column(name = "excelName")
    @NoCSVDelimiter
    private String excelMappingName;
    @Column(name = "cmsName")
    @NoCSVDelimiter
    private String cmsMappingName;
    @Column(name = "ensembleCategory")
    private String ensembleCategory;

    public ConfigurableEnsemble() {
        // nop
    }
       
        
    public ConfigurableEnsemble(String klarname, String excelMappingName, String cmsMappingName, String ensembleCategoryName) {
        this.klarname = klarname;
        this.excelMappingName = excelMappingName;
        this.cmsMappingName = cmsMappingName;
        this.ensembleCategory = ensembleCategoryName.toUpperCase();
    }
    
    

    @Override
    public String getName() {
        return this.klarname;
    }

    @Override
    public EnsembleCategory getEnsembleCategory() {
        return EnsembleCategory.valueOf(this.ensembleCategory.toUpperCase());
    }

    @Override
    public String getExcelMappingName() {
        return this.excelMappingName;
    }

    @Override
    public String getCMSMappingName() {
        return this.cmsMappingName;
    }

    public void setKlarname(String klarname) {
        this.klarname = klarname;
    }

    public void setExcelMappingName(String excelMappingName) {
        this.excelMappingName = excelMappingName;
    }

    public void setCmsMappingName(String cmsMappingName) {
        this.cmsMappingName = cmsMappingName;
    }

    public void setEnsembleCategory(EnsembleCategory ensembleCategory) {
        this.ensembleCategory = ensembleCategory.name();
    }
    
    
    
}
